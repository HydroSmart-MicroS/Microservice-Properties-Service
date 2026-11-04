package com.hydrosmart.properties.application.internal.queryservices;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.hydrosmart.properties.domain.services.AssetRepository;
import com.hydrosmart.properties.domain.model.aggregates.*;
import com.hydrosmart.properties.domain.model.valueobjects.*;
@Component
public class ResourceAccessPolicy {
    private final AssetRepository repository;
    public ResourceAccessPolicy(AssetRepository repository) { this.repository = repository; }
    public Property ownProperty(UUID id, Actor actor) {
        actor.requireManager();
        var property = repository.property(id).orElseThrow(DomainException::notFound);
        if (!property.ownerUserId().equals(actor.userId())) throw DomainException.notFound();
        return property;
    }
    public Unit ownUnit(UUID id, Actor actor) {
        var unit = repository.unit(id).orElseThrow(DomainException::notFound);
        ownProperty(unit.propertyId(), actor);
        return unit;
    }
    public Device ownDevice(UUID id, Actor actor) {
        var device = repository.device(id).orElseThrow(DomainException::notFound);
        ownUnit(device.unitId(),actor);
        return device;
    }
    public UnitAccess visibleUnit(UUID id, Actor actor) {
        return repository.access(id,actor.userId()).orElseThrow(DomainException::notFound);
    }
}
