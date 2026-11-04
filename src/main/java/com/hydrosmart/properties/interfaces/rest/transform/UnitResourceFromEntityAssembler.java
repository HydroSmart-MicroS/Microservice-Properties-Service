package com.hydrosmart.properties.interfaces.rest.transform;
import com.hydrosmart.properties.domain.model.aggregates.Unit;
import com.hydrosmart.properties.interfaces.rest.resources.UnitResource;
public final class UnitResourceFromEntityAssembler {
    private UnitResourceFromEntityAssembler() {}
    public static UnitResource toResource(Unit e) { return new UnitResource(e.id(),e.propertyId(),e.name(),e.createdAt()); }
}
