package com.hydrosmart.properties.application.internal.commandservices;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hydrosmart.properties.domain.services.*;
import com.hydrosmart.properties.domain.model.entities.Tenancy;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.domain.model.valueobjects.*;
import com.hydrosmart.properties.application.internal.queryservices.ResourceAccessPolicy;
import com.hydrosmart.properties.application.internal.outboundservices.IamUserLookup;
@Service @Transactional
public class TenancyCommandServiceImpl implements TenancyCommandService {
    private final AssetRepository repository; private final ResourceAccessPolicy policy; private final IamUserLookup users; private final DeviceQuotaPolicy quota;
    public TenancyCommandServiceImpl(AssetRepository repository, ResourceAccessPolicy policy, IamUserLookup users,DeviceQuotaPolicy quota) { this.repository=repository; this.policy=policy; this.users=users;this.quota=quota; }
    public Tenancy handle(CreateTenancyCommand c) {
        c.actor().requireOwner();
        Rules.required(c.unitId(),"unit"); policy.ownUnit(c.unitId(),c.actor());
        var tenant=Rules.text(c.tenantUserId(),255,"tenant");
        if (!users.isTenant(tenant)) throw new DomainException(DomainException.Kind.INVALID,"tenant_not_found");
        quota.assigning(c.actor().userId(),tenant,c.unitId());
        var t=new Tenancy(UUID.randomUUID(),c.unitId(),tenant,Instant.now(),null); repository.insert(t); return t;
    }
    public void handle(EndTenancyCommand c) {
        c.actor().requireOwner();
        var t=repository.tenancy(c.tenancyId()).orElseThrow(DomainException::notFound);
        policy.ownUnit(t.unitId(),c.actor());
        if (t.endsAt()==null) repository.endTenancy(t.id());
    }
}
