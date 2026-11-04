package com.hydrosmart.properties.domain.model.valueobjects;
public final class Rules {
    private Rules() {}
    public static String text(String value, int maximum, String field) {
        if (value == null || value.isBlank() || value.length() > maximum || value.codePoints().anyMatch(Character::isISOControl))
            throw new DomainException(DomainException.Kind.INVALID, "invalid_" + field);
        return value.strip();
    }
    public static <T> T required(T value, String field) {
        if (value == null || (value instanceof java.util.UUID id && id.getMostSignificantBits()==0 && id.getLeastSignificantBits()==0)) throw new DomainException(DomainException.Kind.INVALID, "missing_" + field);
        return value;
    }
}
