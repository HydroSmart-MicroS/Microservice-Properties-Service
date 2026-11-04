package com.hydrosmart.properties.interfaces.acl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.hydrosmart.properties.application.internal.outboundservices.PlanEntitlements;
import com.hydrosmart.properties.shared.infrastructure.messaging.RpcBus;
@Component
public class BillingContextFacade implements PlanEntitlements {
 private final RpcBus bus;
 public BillingContextFacade(RpcBus bus){this.bus=bus;}
 public int deviceLimit(String user){
  try{var data=bus.get("billing","/internal/v1/billing/users/"+java.net.URLEncoder.encode(user,java.nio.charset.StandardCharsets.UTF_8),bus.internalHeaders());return Boolean.TRUE.equals(data.get("active"))?((Number)data.get("deviceLimit")).intValue():0;}
  catch(Exception error){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"billing_broker_unavailable");}
 }
}

