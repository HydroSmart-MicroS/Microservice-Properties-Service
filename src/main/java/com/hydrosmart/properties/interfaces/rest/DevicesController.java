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

import static com.hydrosmart.properties.interfaces.rest.transform.DeviceResourceFromEntityAssembler.toResource;
@RestController @RequestMapping("/api/v1/devices")
public class DevicesController {
    private final DeviceCommandService commands; private final AssetQueryService queries;
    public DevicesController(DeviceCommandService commands,AssetQueryService queries) { this.commands=commands; this.queries=queries; }
    @PostMapping public ResponseEntity<DeviceResource> create(@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody CreateDeviceResource resource) {
        var result=toResource(commands.handle(toCommand(resource,toActor(jwt))));
        return ResponseEntity.created(java.net.URI.create("/api/v1/devices/"+result.id())).body(result);
    }
    @GetMapping public List<DeviceResource> list(@AuthenticationPrincipal Jwt jwt,@RequestParam(required=false) UUID unitId) {
        return queries.handle(new GetDevicesQuery(toActor(jwt),unitId)).stream().map(DeviceResourceFromEntityAssembler::toResource).toList();
    }
    @GetMapping("/{id}") public DeviceResource get(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id) {
        return toResource(queries.handle(new GetDeviceByIdQuery(toActor(jwt),id)));
    }
    @PutMapping("/{id}") public DeviceResource update(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id,@Valid @RequestBody UpdateDeviceResource resource) {
        return toResource(commands.handle(toCommand(resource,toActor(jwt),id)));
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id) {
        commands.handle(new DisableDeviceCommand(toActor(jwt),id));
        return ResponseEntity.noContent().build();
    }
}
