package com.hydrosmart.properties.domain.model.aggregates;
import java.util.UUID;
import java.time.Instant;
import com.hydrosmart.properties.domain.model.valueobjects.*;
public record Unit(UUID id, UUID propertyId, String name, Instant createdAt) {
    public Unit { Rules.required(id,"id"); Rules.required(propertyId,"property"); name = Rules.text(name,80,"name"); }
}
