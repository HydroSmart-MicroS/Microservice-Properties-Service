package com.hydrosmart.properties.domain.model.valueobjects;
import java.time.Instant;
import java.util.UUID;
/** Owners see all history. Active tenants see only readings since their current tenancy. */
public record UnitAccess(UUID unitId, String ownerUserId, Instant visibleFrom) {}
