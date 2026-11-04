package com.hydrosmart.properties.interfaces.acl;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.hydrosmart.properties.application.internal.outboundservices.IamUserLookup;
import com.hydrosmart.properties.shared.infrastructure.messaging.RpcBus;
@Component
public class IamContextFacade implements IamUserLookup {
 private final RpcBus bus;
 public IamContextFacade(RpcBus bus){this.bus=bus;}
 public boolean isTenant(String userId){
  try{
   var response=bus.request("identity","GET","/internal/v1/tenants/"+URLEncoder.encode(userId,StandardCharsets.UTF_8),bus.internalHeaders(),null);
   if(response.status()==404)return false;
   if(response.status()!=200)throw new IllegalStateException();
   return true;
  }catch(Exception error){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"identity_broker_unavailable");}
 }
}

