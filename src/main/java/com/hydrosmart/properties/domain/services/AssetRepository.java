package com.hydrosmart.properties.domain.services;
import java.util.*;
import com.hydrosmart.properties.domain.model.aggregates.*;
import com.hydrosmart.properties.domain.model.entities.Tenancy;
import com.hydrosmart.properties.domain.model.valueobjects.UnitAccess;
/** Port implemented by this service's PostgreSQL adapter. */
public interface AssetRepository {
    Map<String,Object> deviceUsage(String userId);
    Map<String,Object> unitSnapshot(UUID id);
    List<String> unitTenants(UUID id);
    int unitDeviceCount(UUID id);
    void lockQuota(String userId);
    Optional<Property> property(UUID id);
    Optional<Unit> unit(UUID id);
    Optional<Device> device(UUID id);
    Optional<Tenancy> tenancy(UUID id);
    Optional<UnitAccess> access(UUID unitId, String userId);
    List<Property> properties(String userId);
    List<Unit> units(String userId, UUID propertyId);
    List<Device> devices(String userId, UUID unitId);
    List<Tenancy> tenancies(UUID unitId);
    void insert(Property property); void update(Property property); void deleteProperty(UUID id);
    void insert(Unit unit); void update(Unit unit); void deleteUnit(UUID id);
    void insert(Device device); void update(Device device);
    void insert(Tenancy tenancy); void endTenancy(UUID id);
}
