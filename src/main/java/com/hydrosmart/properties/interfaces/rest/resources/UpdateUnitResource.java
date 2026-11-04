package com.hydrosmart.properties.interfaces.rest.resources;
import java.util.UUID;
import java.time.Instant;
import jakarta.validation.constraints.*;
public record UpdateUnitResource(@NotBlank @Size(max=80) String name) {}
