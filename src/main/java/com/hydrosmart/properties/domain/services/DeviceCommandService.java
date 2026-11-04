package com.hydrosmart.properties.domain.services;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.domain.model.aggregates.*;
import com.hydrosmart.properties.domain.model.entities.Tenancy;
public interface DeviceCommandService { Device handle(CreateDeviceCommand command); Device handle(UpdateDeviceCommand command); void handle(DisableDeviceCommand command); }
