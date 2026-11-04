package com.hydrosmart.properties.interfaces.rest;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import com.hydrosmart.properties.domain.services.*;
import com.hydrosmart.properties.domain.model.queries.GetUnitAccessQuery;
import com.hydrosmart.properties.domain.model.valueobjects.*;
import com.hydrosmart.properties.interfaces.rest.resources.DeviceResource;
import com.hydrosmart.properties.interfaces.rest.transform.*;
@RestController @RequestMapping("/internal/v1")
public class InternalRegistryController {
    private final AssetRepository repository; private final AssetQueryService queries;
    public InternalRegistryController(AssetRepository repository,AssetQueryService queries) { this.repository=repository; this.queries=queries; }
    @GetMapping("/users/{user}/devices") public java.util.Map<String,Object> usage(@PathVariable String user){return repository.deviceUsage(user);}
    @GetMapping("/units/{id}/snapshot") public java.util.Map<String,Object> snapshot(@PathVariable UUID id){return repository.unitSnapshot(id);}
    @GetMapping("/devices/{id}") public DeviceResource resolve(@PathVariable UUID id) {
        return DeviceResourceFromEntityAssembler.toResource(repository.device(id).orElseThrow(DomainException::notFound));
    }
    @GetMapping("/units/{id}/access") public UnitAccess access(@PathVariable UUID id,@AuthenticationPrincipal Jwt jwt) {
        return queries.handle(new GetUnitAccessQuery(ActorFromJwtAssembler.toActor(jwt),id));
    }
}
