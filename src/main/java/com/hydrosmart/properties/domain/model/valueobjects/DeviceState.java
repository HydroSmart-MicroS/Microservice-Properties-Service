package com.hydrosmart.properties.domain.model.valueobjects;
public enum DeviceState {
    registered, active, disabled;
    public static DeviceState parse(String value) {
        try { return valueOf(value); }
        catch (IllegalArgumentException | NullPointerException error) {
            throw new DomainException(DomainException.Kind.INVALID, "invalid_device_state");
        }
    }
}
