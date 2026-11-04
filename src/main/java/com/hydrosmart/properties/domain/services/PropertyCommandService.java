package com.hydrosmart.properties.domain.services;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.domain.model.aggregates.*;
import com.hydrosmart.properties.domain.model.entities.Tenancy;
public interface PropertyCommandService { Property handle(CreatePropertyCommand command); Property handle(UpdatePropertyCommand command); void handle(DeletePropertyCommand command); }
