package com.hydrosmart.properties.application.internal.commandservices;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hydrosmart.properties.domain.services.*;
import com.hydrosmart.properties.domain.model.aggregates.Device;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.domain.model.valueobjects.*;
import com.hydrosmart.properties.application.internal.queryservices.ResourceAccessPolicy;
@Service @Transactional
public class DeviceCommandServiceImpl implements DeviceCommandService {
    private final AssetRepository repository; private final ResourceAccessPolicy policy; private final DeviceQuotaPolicy quota;
    public DeviceCommandServiceImpl(AssetRepository repository, ResourceAccessPolicy policy,DeviceQuotaPolicy quota) { this.repository=repository; this.policy=policy; this.quota=quota; }
    public Device handle(CreateDeviceCommand c) {
        Rules.required(c.unitId(),"unit"); policy.ownUnit(c.unitId(),c.actor());
        quota.addingDevice(c.actor().userId(),c.unitId());
        var d=new Device(c.deviceId()==null?UUID.randomUUID():c.deviceId(),c.unitId(),c.serialNumber(),c.name(),c.category(),DeviceState.registered,Instant.now());
        repository.insert(d); return d;
    }
    public Device handle(UpdateDeviceCommand c) {
        var old=policy.ownDevice(c.deviceId(),c.actor());
        if(old.state()==DeviceState.disabled && DeviceState.parse(c.state())!=DeviceState.disabled)quota.addingDevice(c.actor().userId(),old.unitId());
        var d=new Device(old.id(),old.unitId(),old.serialNumber(),c.name(),c.category(),DeviceState.parse(c.state()),old.registeredAt());
        repository.update(d); return d;
    }
    public void handle(DisableDeviceCommand c) {
        var old=policy.ownDevice(c.deviceId(),c.actor());
        repository.update(new Device(old.id(),old.unitId(),old.serialNumber(),old.name(),old.category(),DeviceState.disabled,old.registeredAt()));
    }
}
