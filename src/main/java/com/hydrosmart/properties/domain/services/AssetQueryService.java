package com.hydrosmart.properties.domain.services;
import java.util.*;
import com.hydrosmart.properties.domain.model.aggregates.*;
import com.hydrosmart.properties.domain.model.entities.Tenancy;
import com.hydrosmart.properties.domain.model.queries.*;
import com.hydrosmart.properties.domain.model.valueobjects.UnitAccess;
public interface AssetQueryService {
    List<Property> handle(GetPropertiesQuery query); Property handle(GetPropertyByIdQuery query);
    List<Unit> handle(GetUnitsQuery query); Unit handle(GetUnitByIdQuery query);
    List<Device> handle(GetDevicesQuery query); Device handle(GetDeviceByIdQuery query);
    List<Tenancy> handle(GetTenanciesQuery query); UnitAccess handle(GetUnitAccessQuery query);
}
