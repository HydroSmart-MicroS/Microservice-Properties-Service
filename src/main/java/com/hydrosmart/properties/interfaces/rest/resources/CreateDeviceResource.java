package com.hydrosmart.properties.interfaces.rest.resources;
import java.util.UUID;
import java.time.Instant;
import jakarta.validation.constraints.*;
public record CreateDeviceResource(@NotNull UUID unitId, UUID deviceId, @NotBlank @Size(max=120) String serialNumber, @NotBlank @Size(max=120) String name, @NotBlank @Size(max=80) String category) {}
