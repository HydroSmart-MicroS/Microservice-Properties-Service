package com.hydrosmart.properties.domain.services;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.domain.model.aggregates.*;
import com.hydrosmart.properties.domain.model.entities.Tenancy;
public interface UnitCommandService { Unit handle(CreateUnitCommand command); Unit handle(UpdateUnitCommand command); void handle(DeleteUnitCommand command); }
