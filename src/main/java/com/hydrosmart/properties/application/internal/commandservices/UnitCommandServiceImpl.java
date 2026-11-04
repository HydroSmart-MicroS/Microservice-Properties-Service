package com.hydrosmart.properties.application.internal.commandservices;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hydrosmart.properties.domain.services.*;
import com.hydrosmart.properties.domain.model.aggregates.Unit;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.domain.model.valueobjects.Rules;
import com.hydrosmart.properties.application.internal.queryservices.ResourceAccessPolicy;
@Service @Transactional
public class UnitCommandServiceImpl implements UnitCommandService {
    private final AssetRepository repository; private final ResourceAccessPolicy policy;
    public UnitCommandServiceImpl(AssetRepository repository, ResourceAccessPolicy policy) { this.repository=repository; this.policy=policy; }
    public Unit handle(CreateUnitCommand c) {
        Rules.required(c.propertyId(),"property"); policy.ownProperty(c.propertyId(),c.actor());
        var u=new Unit(UUID.randomUUID(),c.propertyId(),c.name(),Instant.now()); repository.insert(u); return u;
    }
    public Unit handle(UpdateUnitCommand c) {
        var old=policy.ownUnit(c.unitId(),c.actor());
        var u=new Unit(old.id(),old.propertyId(),c.name(),old.createdAt()); repository.update(u); return u;
    }
    public void handle(DeleteUnitCommand c) { policy.ownUnit(c.unitId(),c.actor()); repository.deleteUnit(c.unitId()); }
}
