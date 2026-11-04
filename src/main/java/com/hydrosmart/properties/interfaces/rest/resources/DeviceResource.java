package com.hydrosmart.properties.interfaces.rest.resources;
import java.util.UUID;
import java.time.Instant;
import jakarta.validation.constraints.*;
public record DeviceResource(UUID id, UUID unitId, String serialNumber, String name, String category, String state, Instant registeredAt) {}
