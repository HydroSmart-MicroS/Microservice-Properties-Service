package com.hydrosmart.properties.interfaces.rest.resources;
import java.util.UUID;
import java.time.Instant;
import jakarta.validation.constraints.*;
public record PropertyResource(UUID id, String ownerUserId, String name, String address, Instant createdAt) {}
