package com.hydrosmart.properties.domain.model.queries;
import java.util.UUID;
import com.hydrosmart.properties.domain.model.valueobjects.Actor;
public record GetDeviceByIdQuery(Actor actor, UUID deviceId) {}
