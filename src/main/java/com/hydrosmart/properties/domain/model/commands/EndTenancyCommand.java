package com.hydrosmart.properties.domain.model.commands;
import java.util.UUID;
import com.hydrosmart.properties.domain.model.valueobjects.Actor;
public record EndTenancyCommand(Actor actor, UUID tenancyId) {}
