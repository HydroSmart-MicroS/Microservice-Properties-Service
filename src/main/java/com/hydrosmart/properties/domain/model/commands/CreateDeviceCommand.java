package com.hydrosmart.properties.domain.model.commands;
import java.util.UUID;
import com.hydrosmart.properties.domain.model.valueobjects.Actor;
public record CreateDeviceCommand(Actor actor, UUID unitId, UUID deviceId, String serialNumber, String name, String category) {}
