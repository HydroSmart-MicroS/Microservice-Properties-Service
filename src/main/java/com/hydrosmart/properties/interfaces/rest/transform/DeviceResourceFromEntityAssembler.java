package com.hydrosmart.properties.interfaces.rest.transform;
import com.hydrosmart.properties.domain.model.aggregates.Device;
import com.hydrosmart.properties.interfaces.rest.resources.DeviceResource;
public final class DeviceResourceFromEntityAssembler {
    private DeviceResourceFromEntityAssembler() {}
    public static DeviceResource toResource(Device e) { return new DeviceResource(e.id(),e.unitId(),e.serialNumber(),e.name(),e.category(),e.state().name(),e.registeredAt()); }
}
