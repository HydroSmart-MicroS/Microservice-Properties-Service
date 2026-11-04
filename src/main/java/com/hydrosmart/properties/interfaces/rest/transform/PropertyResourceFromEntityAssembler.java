package com.hydrosmart.properties.interfaces.rest.transform;
import com.hydrosmart.properties.domain.model.aggregates.Property;
import com.hydrosmart.properties.interfaces.rest.resources.PropertyResource;
public final class PropertyResourceFromEntityAssembler {
    private PropertyResourceFromEntityAssembler() {}
    public static PropertyResource toResource(Property e) { return new PropertyResource(e.id(),e.ownerUserId(),e.name(),e.address(),e.createdAt()); }
}
