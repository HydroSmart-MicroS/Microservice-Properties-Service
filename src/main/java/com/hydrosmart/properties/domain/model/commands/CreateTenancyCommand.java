package com.hydrosmart.properties.domain.model.commands;
import java.util.UUID;
import com.hydrosmart.properties.domain.model.valueobjects.Actor;
public record CreateTenancyCommand(Actor actor, UUID unitId, String tenantUserId) {}
