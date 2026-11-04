package com.hydrosmart.properties.interfaces.rest.transform;
import com.hydrosmart.properties.domain.model.entities.Tenancy;
import com.hydrosmart.properties.interfaces.rest.resources.TenancyResource;
public final class TenancyResourceFromEntityAssembler {
    private TenancyResourceFromEntityAssembler() {}
    public static TenancyResource toResource(Tenancy e) { return new TenancyResource(e.id(),e.unitId(),e.tenantUserId(),e.startsAt(),e.endsAt()); }
}
