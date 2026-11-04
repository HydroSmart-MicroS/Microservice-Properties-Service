package com.hydrosmart.properties.domain.model.commands;
import java.util.UUID;
import com.hydrosmart.properties.domain.model.valueobjects.Actor;
public record DeleteUnitCommand(Actor actor, UUID unitId) {}
