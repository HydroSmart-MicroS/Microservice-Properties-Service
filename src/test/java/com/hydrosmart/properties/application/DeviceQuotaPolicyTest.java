package com.hydrosmart.properties.application;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.hydrosmart.properties.application.internal.commandservices.DeviceQuotaPolicy;
import com.hydrosmart.properties.application.internal.outboundservices.PlanEntitlements;
import com.hydrosmart.properties.domain.services.AssetRepository;
import com.hydrosmart.properties.domain.model.valueobjects.DomainException;

class DeviceQuotaPolicyTest {
 final AssetRepository repository=mock(AssetRepository.class);
 final PlanEntitlements plans=mock(PlanEntitlements.class);
 final DeviceQuotaPolicy policy=new DeviceQuotaPolicy(repository,plans);
 final UUID unit=UUID.randomUUID();

 @Test void readsCurrentTenantsAfterLockingOwnerAndRejectsTheirFullQuota(){
  var assignmentCommitted=new AtomicBoolean(false);
  doAnswer(invocation->{assignmentCommitted.set(true);return null;}).when(repository).lockQuota("z-owner");
  when(repository.unitTenants(unit)).thenAnswer(invocation->assignmentCommitted.get()?List.of("a-tenant"):List.of());
  when(plans.deviceLimit(anyString())).thenReturn(1);
  when(repository.deviceUsage("z-owner")).thenReturn(Map.of("count",0));
  when(repository.deviceUsage("a-tenant")).thenReturn(Map.of("count",1));
  var error=assertThrows(DomainException.class,()->policy.addingDevice("z-owner",unit));
  assertEquals("device_quota_exceeded",error.getMessage());
 }

 @Test void assignmentIncludesMetersFromOtherUnitsInTenantQuota(){
  when(plans.deviceLimit("a-tenant")).thenReturn(4);
  when(repository.unitDeviceCount(unit)).thenReturn(2);
  when(repository.deviceUsage("a-tenant")).thenReturn(Map.of("count",3));
  var error=assertThrows(DomainException.class,()->policy.assigning("z-owner","a-tenant",unit));
  assertEquals("device_quota_exceeded",error.getMessage());
 }
}
