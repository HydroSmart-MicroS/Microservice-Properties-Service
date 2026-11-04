package com.hydrosmart.properties.domain.model.valueobjects;
public record Actor(String userId, String profileType) {
    public Actor {
        if (userId == null || userId.isBlank() || profileType == null || !java.util.Set.of("propietario","inquilino").contains(profileType))
            throw new DomainException(DomainException.Kind.FORBIDDEN, "invalid_identity");
    }
    public void requireManager() {
        if ("inquilino".equals(profileType)) throw new DomainException(DomainException.Kind.FORBIDDEN, "manager_required");
    }
    public void requireOwner() {
        if (!"propietario".equals(profileType)) throw new DomainException(DomainException.Kind.FORBIDDEN, "owner_required");
    }
}
