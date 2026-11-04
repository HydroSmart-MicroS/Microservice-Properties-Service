package com.hydrosmart.properties.domain.model.queries;
import java.util.UUID;
import com.hydrosmart.properties.domain.model.valueobjects.Actor;
public record GetDevicesQuery(Actor actor, UUID unitId) {}
