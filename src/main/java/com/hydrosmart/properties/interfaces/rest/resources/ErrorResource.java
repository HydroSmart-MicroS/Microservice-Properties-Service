package com.hydrosmart.properties.interfaces.rest.resources;
import java.util.UUID;
import java.time.Instant;
import jakarta.validation.constraints.*;
public record ErrorResource(String error) {}
