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

import static com.hydrosmart.properties.interfaces.rest.transform.UnitResourceFromEntityAssembler.toResource;
@RestController @RequestMapping("/api/v1/units")
public class UnitsController {
    private final UnitCommandService commands; private final AssetQueryService queries;
    public UnitsController(UnitCommandService commands,AssetQueryService queries) { this.commands=commands; this.queries=queries; }
    @PostMapping public ResponseEntity<UnitResource> create(@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody CreateUnitResource resource) {
        var result=toResource(commands.handle(toCommand(resource,toActor(jwt))));
        return ResponseEntity.created(java.net.URI.create("/api/v1/units/"+result.id())).body(result);
    }
    @GetMapping public List<UnitResource> list(@AuthenticationPrincipal Jwt jwt,@RequestParam(required=false) UUID propertyId) {
        return queries.handle(new GetUnitsQuery(toActor(jwt),propertyId)).stream().map(UnitResourceFromEntityAssembler::toResource).toList();
    }
    @GetMapping("/{id}") public UnitResource get(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id) {
        return toResource(queries.handle(new GetUnitByIdQuery(toActor(jwt),id)));
    }
    @PutMapping("/{id}") public UnitResource update(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id,@Valid @RequestBody UpdateUnitResource resource) {
        return toResource(commands.handle(toCommand(resource,toActor(jwt),id)));
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id) {
        commands.handle(new DeleteUnitCommand(toActor(jwt),id));
        return ResponseEntity.noContent().build();
    }
}
