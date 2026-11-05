package com.hydrosmart.properties.domain.model.valueobjects;
import java.util.*;
import com.hydrosmart.properties.domain.model.aggregates.Device;
public final class DeviceZonePolicy {
 private DeviceZonePolicy(){}
 public static void validate(List<Device> devices,UUID changing,String category,DeviceState state){
  var remaining=devices.stream().filter(d->!d.id().equals(changing)&&d.state()!=DeviceState.disabled).toList();
  if(state!=DeviceState.disabled){
   if(category.equals("GENERAL")&&remaining.stream().anyMatch(d->d.category().equalsIgnoreCase("GENERAL")))
    throw new DomainException(DomainException.Kind.CONFLICT,"La unidad ya tiene un medidor general. Puedes editar su nombre y ubicación.");
   if(!category.equals("GENERAL")&&remaining.stream().noneMatch(d->d.category().equalsIgnoreCase("GENERAL")))
    throw new DomainException(DomainException.Kind.CONFLICT,"Primero elige el medidor GENERAL de esta unidad desde Medidores.");
  }
  boolean removingGeneral=devices.stream().anyMatch(d->d.id().equals(changing)&&d.state()!=DeviceState.disabled&&d.category().equalsIgnoreCase("GENERAL"))
      &&(state==DeviceState.disabled||!category.equals("GENERAL"));
  if(removingGeneral&&!remaining.isEmpty()&&remaining.stream().noneMatch(d->d.category().equalsIgnoreCase("GENERAL")))throw new DomainException(DomainException.Kind.CONFLICT,"Desactiva primero los medidores por zona para retirar el medidor general. Su historial se conservará.");
 }
}
