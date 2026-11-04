package com.hydrosmart.properties.interfaces.rest.resources;
import java.util.UUID;
import java.time.Instant;
import jakarta.validation.constraints.*;
public record CreateTenancyResource(@NotNull UUID unitId, @NotBlank @Size(max=255) String tenantUserId) {}
