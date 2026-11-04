package com.hydrosmart.properties.interfaces.rest.resources;
import java.util.UUID;
import java.time.Instant;
import jakarta.validation.constraints.*;
public record TenancyResource(UUID id, UUID unitId, String tenantUserId, Instant startsAt, Instant endsAt) {}
