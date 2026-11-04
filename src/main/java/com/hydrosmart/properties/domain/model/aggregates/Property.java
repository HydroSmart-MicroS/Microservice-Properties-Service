package com.hydrosmart.properties.domain.model.aggregates;
import java.util.UUID;
import java.time.Instant;
import com.hydrosmart.properties.domain.model.valueobjects.*;
public record Property(UUID id, String ownerUserId, String name, String address, Instant createdAt) {
    public Property { Rules.required(id,"id"); Rules.required(ownerUserId,"owner"); name = Rules.text(name,120,"name"); address = Rules.text(address,500,"address"); }
}
