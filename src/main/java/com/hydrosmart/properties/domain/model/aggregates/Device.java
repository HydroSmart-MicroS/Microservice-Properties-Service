package com.hydrosmart.properties.domain.model.aggregates;
import java.util.UUID;
import java.time.Instant;
import com.hydrosmart.properties.domain.model.valueobjects.*;
public record Device(UUID id, UUID unitId, String serialNumber, String name, String category, DeviceState state, Instant registeredAt) {
    public Device { Rules.required(id,"id"); Rules.required(unitId,"unit"); serialNumber = Rules.text(serialNumber,120,"serial_number"); name = Rules.text(name,120,"name"); category = Rules.text(category,80,"category"); Rules.required(state,"state"); }
}
