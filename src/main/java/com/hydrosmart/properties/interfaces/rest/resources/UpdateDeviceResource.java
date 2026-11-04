package com.hydrosmart.properties.interfaces.rest.resources;
import java.util.UUID;
import java.time.Instant;
import jakarta.validation.constraints.*;
public record UpdateDeviceResource(@NotBlank @Size(max=120) String name, @NotBlank @Size(max=80) String category, @NotBlank String state) {}
