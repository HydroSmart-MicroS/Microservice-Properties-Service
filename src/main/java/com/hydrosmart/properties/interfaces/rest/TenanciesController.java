package com.hydrosmart.properties.interfaces.rest;
import java.util.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.ResponseEntity;
import com.hydrosmart.properties.domain.services.*;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.domain.model.queries.*;
import com.hydrosmart.properties.interfaces.rest.resources.*;
import com.hydrosmart.properties.interfaces.rest.transform.*;
import static com.hydrosmart.properties.interfaces.rest.transform.ActorFromJwtAssembler.toActor;
import static com.hydrosmart.properties.interfaces.rest.transform.CommandFromResourceAssembler.toCommand;

import static com.hydrosmart.properties.interfaces.rest.transform.TenancyResourceFromEntityAssembler.toResource;
@RestController @RequestMapping("/api/v1/tenancies")
public class TenanciesController {
    private final TenancyCommandService commands; private final AssetQueryService queries;
    public TenanciesController(TenancyCommandService commands,AssetQueryService queries) { this.commands=commands; this.queries=queries; }
    @PostMapping public ResponseEntity<TenancyResource> create(@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody CreateTenancyResource r) {
        var result=toResource(commands.handle(toCommand(r,toActor(jwt))));
        return ResponseEntity.created(java.net.URI.create("/api/v1/tenancies/"+result.id())).body(result);
    }
    @GetMapping public List<TenancyResource> list(@AuthenticationPrincipal Jwt jwt,@RequestParam UUID unitId) {
        return queries.handle(new GetTenanciesQuery(toActor(jwt),unitId)).stream().map(TenancyResourceFromEntityAssembler::toResource).toList();
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> end(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id) {
        commands.handle(new EndTenancyCommand(toActor(jwt),id)); return ResponseEntity.noContent().build();
    }
}
