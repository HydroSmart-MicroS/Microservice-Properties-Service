package com.hydrosmart.properties.domain.model.valueobjects;
import java.util.Locale;
public final class DeviceZone {
 private DeviceZone(){}
 public static String parse(String value){
  return switch(value==null?"":value.strip().toUpperCase(Locale.ROOT)){
   case "GENERAL"->"GENERAL";case "RIEGO"->"RIEGO";case "COCINA"->"COCINA";case "BAÑO","BANO"->"BAÑO";
   default->throw new DomainException(DomainException.Kind.INVALID,"Elige General, Riego, Cocina o Baño.");
  };
 }
}
