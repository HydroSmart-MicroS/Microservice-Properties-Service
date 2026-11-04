package com.hydrosmart.properties.interfaces.rest.resources;
import java.util.UUID;
import java.time.Instant;
import jakarta.validation.constraints.*;
public record UpdatePropertyResource(@NotBlank @Size(max=120) String name, @NotBlank @Size(max=500) String address) {}
