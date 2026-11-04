package com.hydrosmart.properties.application.internal.queryservices;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hydrosmart.properties.domain.services.*;
import com.hydrosmart.properties.domain.model.aggregates.*;
import com.hydrosmart.properties.domain.model.entities.Tenancy;
import com.hydrosmart.properties.domain.model.queries.*;
import com.hydrosmart.properties.domain.model.valueobjects.*;
@Service
@Transactional(readOnly=true)
public class AssetQueryServiceImpl implements AssetQueryService {
    private final AssetRepository repository; private final ResourceAccessPolicy policy;
    public AssetQueryServiceImpl(AssetRepository repository, ResourceAccessPolicy policy) { this.repository=repository; this.policy=policy; }
    public List<Property> handle(GetPropertiesQuery q) { return repository.properties(q.actor().userId()); }
    public Property handle(GetPropertyByIdQuery q) {
        var property=repository.property(q.propertyId()).orElseThrow(DomainException::notFound);
        if (!property.ownerUserId().equals(q.actor().userId()) && repository.units(q.actor().userId(),q.propertyId()).isEmpty()) throw DomainException.notFound();
        return property;
    }
    public List<Unit> handle(GetUnitsQuery q) { return repository.units(q.actor().userId(),q.propertyId()); }
    public Unit handle(GetUnitByIdQuery q) {
        policy.visibleUnit(q.unitId(),q.actor()); return repository.unit(q.unitId()).orElseThrow(DomainException::notFound);
    }
    public List<Device> handle(GetDevicesQuery q) {
        if (q.unitId()!=null) policy.visibleUnit(q.unitId(),q.actor());
        return repository.devices(q.actor().userId(),q.unitId());
    }
    public Device handle(GetDeviceByIdQuery q) {
        var d=repository.device(q.deviceId()).orElseThrow(DomainException::notFound);
        policy.visibleUnit(d.unitId(),q.actor()); return d;
    }
    public List<Tenancy> handle(GetTenanciesQuery q) { q.actor().requireOwner(); policy.ownUnit(q.unitId(),q.actor()); return repository.tenancies(q.unitId()); }
    public UnitAccess handle(GetUnitAccessQuery q) { return policy.visibleUnit(q.unitId(),q.actor()); }
}
