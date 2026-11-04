package com.hydrosmart.properties.domain.model.commands;
import java.util.UUID;
import com.hydrosmart.properties.domain.model.valueobjects.Actor;
public record UpdatePropertyCommand(Actor actor, UUID propertyId, String name, String address) {}
