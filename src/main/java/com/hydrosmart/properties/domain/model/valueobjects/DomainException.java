package com.hydrosmart.properties.domain.model.valueobjects;
public final class DomainException extends RuntimeException {
    public enum Kind { INVALID, FORBIDDEN, NOT_FOUND, CONFLICT }
    private final Kind kind;
    public DomainException(Kind kind, String message) { super(message); this.kind = kind; }
    public Kind kind() { return kind; }
    public static DomainException notFound() { return new DomainException(Kind.NOT_FOUND, "resource_not_found"); }
}
