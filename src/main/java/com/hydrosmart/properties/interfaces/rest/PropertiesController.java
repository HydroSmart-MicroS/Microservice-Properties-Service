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

import static com.hydrosmart.properties.interfaces.rest.transform.PropertyResourceFromEntityAssembler.toResource;
@RestController @RequestMapping("/api/v1/properties")
public class PropertiesController {
    private final PropertyCommandService commands; private final AssetQueryService queries;
    public PropertiesController(PropertyCommandService commands,AssetQueryService queries) { this.commands=commands; this.queries=queries; }
    @PostMapping public ResponseEntity<PropertyResource> create(@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody CreatePropertyResource resource) {
        var result=toResource(commands.handle(toCommand(resource,toActor(jwt))));
        return ResponseEntity.created(java.net.URI.create("/api/v1/properties/"+result.id())).body(result);
    }
    @GetMapping public List<PropertyResource> list(@AuthenticationPrincipal Jwt jwt) {
        return queries.handle(new GetPropertiesQuery(toActor(jwt))).stream().map(PropertyResourceFromEntityAssembler::toResource).toList();
    }
    @GetMapping("/{id}") public PropertyResource get(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id) {
        return toResource(queries.handle(new GetPropertyByIdQuery(toActor(jwt),id)));
    }
    @PutMapping("/{id}") public PropertyResource update(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id,@Valid @RequestBody UpdatePropertyResource resource) {
        return toResource(commands.handle(toCommand(resource,toActor(jwt),id)));
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id) {
        commands.handle(new DeletePropertyCommand(toActor(jwt),id));
        return ResponseEntity.noContent().build();
    }
}
