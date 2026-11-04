package com.hydrosmart.properties.domain.model.commands;
import java.util.UUID;
import com.hydrosmart.properties.domain.model.valueobjects.Actor;
public record UpdateDeviceCommand(Actor actor, UUID deviceId, String name, String category, String state) {}
