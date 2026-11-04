package com.hydrosmart.properties.application.internal.commandservices;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hydrosmart.properties.domain.services.*;
import com.hydrosmart.properties.domain.model.aggregates.Property;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.application.internal.queryservices.ResourceAccessPolicy;
@Service @Transactional
public class PropertyCommandServiceImpl implements PropertyCommandService {
    private final AssetRepository repository; private final ResourceAccessPolicy policy;
    public PropertyCommandServiceImpl(AssetRepository repository, ResourceAccessPolicy policy) { this.repository=repository; this.policy=policy; }
    public Property handle(CreatePropertyCommand c) {
        c.actor().requireManager();
        var p=new Property(UUID.randomUUID(),c.actor().userId(),c.name(),c.address(),Instant.now());
        repository.insert(p); return p;
    }
    public Property handle(UpdatePropertyCommand c) {
        var old=policy.ownProperty(c.propertyId(),c.actor());
        var p=new Property(old.id(),old.ownerUserId(),c.name(),c.address(),old.createdAt());
        repository.update(p); return p;
    }
    public void handle(DeletePropertyCommand c) { policy.ownProperty(c.propertyId(),c.actor()); repository.deleteProperty(c.propertyId()); }
}
