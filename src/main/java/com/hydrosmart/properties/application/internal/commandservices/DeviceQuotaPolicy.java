package com.hydrosmart.properties.application.internal.commandservices;
import java.util.*;
import org.springframework.stereotype.Component;
import com.hydrosmart.properties.application.internal.outboundservices.PlanEntitlements;
import com.hydrosmart.properties.domain.services.AssetRepository;
import com.hydrosmart.properties.domain.model.valueobjects.DomainException;
@Component
public class DeviceQuotaPolicy {
 private final AssetRepository repository;private final PlanEntitlements plans;
 public DeviceQuotaPolicy(AssetRepository repository,PlanEntitlements plans){this.repository=repository;this.plans=plans;}
 public void addingDevice(String owner,UUID unit){
  // Lock the owner before reading tenants so a concurrent assignment cannot escape its quota check.
  repository.lockQuota(owner);
  var users=new TreeSet<String>(repository.unitTenants(unit));users.remove(owner);
  users.forEach(repository::lockQuota);users.add(owner);
  for(String user:users)check(user,1);
 }
 public void assigning(String owner,String tenant,UUID unit){
  repository.lockQuota(owner);if(!owner.equals(tenant))repository.lockQuota(tenant);
  check(tenant,repository.unitDeviceCount(unit));
 }
 private void check(String user,int additional){
  int limit=plans.deviceLimit(user);
  if(limit<1)throw new DomainException(DomainException.Kind.CONFLICT,"account_plan_required");
  int used=((Number)repository.deviceUsage(user).get("count")).intValue();
  if(used+additional>limit)throw new DomainException(DomainException.Kind.CONFLICT,"device_quota_exceeded");
 }
}

