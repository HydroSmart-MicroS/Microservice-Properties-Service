package com.hydrosmart.properties.interfaces.rest.transform;
import java.util.UUID;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.domain.model.valueobjects.Actor;
import com.hydrosmart.properties.interfaces.rest.resources.*;
public final class CommandFromResourceAssembler {
    private CommandFromResourceAssembler() {}
    public static CreatePropertyCommand toCommand(CreatePropertyResource r,Actor a) { return new CreatePropertyCommand(a,r.name(),r.address()); }
    public static UpdatePropertyCommand toCommand(UpdatePropertyResource r,Actor a,UUID id) { return new UpdatePropertyCommand(a,id,r.name(),r.address()); }
    public static CreateUnitCommand toCommand(CreateUnitResource r,Actor a) { return new CreateUnitCommand(a,r.propertyId(),r.name()); }
    public static UpdateUnitCommand toCommand(UpdateUnitResource r,Actor a,UUID id) { return new UpdateUnitCommand(a,id,r.name()); }
    public static CreateDeviceCommand toCommand(CreateDeviceResource r,Actor a) { return new CreateDeviceCommand(a,r.unitId(),r.deviceId(),r.serialNumber(),r.name(),r.category()); }
    public static UpdateDeviceCommand toCommand(UpdateDeviceResource r,Actor a,UUID id) { return new UpdateDeviceCommand(a,id,r.name(),r.category(),r.state()); }
    public static CreateTenancyCommand toCommand(CreateTenancyResource r,Actor a) { return new CreateTenancyCommand(a,r.unitId(),r.tenantUserId()); }
}
