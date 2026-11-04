package com.hydrosmart.properties.domain.services;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.domain.model.aggregates.*;
import com.hydrosmart.properties.domain.model.entities.Tenancy;
public interface TenancyCommandService { Tenancy handle(CreateTenancyCommand command); void handle(EndTenancyCommand command); }
