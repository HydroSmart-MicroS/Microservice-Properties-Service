package com.hydrosmart.properties.domain.model.entities;
import java.time.Instant;
import java.util.UUID;
import com.hydrosmart.properties.domain.model.valueobjects.Rules;
public record Tenancy(UUID id, UUID unitId, String tenantUserId, Instant startsAt, Instant endsAt) {
    public Tenancy { Rules.required(id,"id"); Rules.required(unitId,"unit"); tenantUserId = Rules.text(tenantUserId,255,"tenant"); Rules.required(startsAt,"starts_at"); }
}
