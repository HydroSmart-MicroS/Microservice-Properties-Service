package com.hydrosmart.properties.domain;
import java.util.*;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.hydrosmart.properties.domain.model.aggregates.Device;
import com.hydrosmart.properties.domain.model.valueobjects.*;
class DeviceZoneTest {
 private Device general(){return new Device(UUID.randomUUID(),UUID.randomUUID(),"serial","Principal","GENERAL",DeviceState.active,Instant.now());}
 @Test void requiresGeneralBeforeZonesAndRejectsDuplicates(){
  assertThrows(DomainException.class,()->DeviceZonePolicy.validate(List.of(),null,"BAÑO",DeviceState.registered));
  var general=general();
  assertDoesNotThrow(()->DeviceZonePolicy.validate(List.of(general),null,"COCINA",DeviceState.active));
  assertThrows(DomainException.class,()->DeviceZonePolicy.validate(List.of(general),null,"GENERAL",DeviceState.active));
 }
 @Test void legacyGeneralSelectionPreservesIdsAndDisablingLastMeterIsAllowed(){
  var old=new Device(UUID.randomUUID(),UUID.randomUUID(),"old","Mi ducha","Personalizada",DeviceState.active,Instant.now());
  assertDoesNotThrow(()->DeviceZonePolicy.validate(List.of(old),old.id(),"GENERAL",DeviceState.active));
  assertDoesNotThrow(()->DeviceZonePolicy.validate(List.of(old),old.id(),old.category(),DeviceState.disabled));
  assertEquals("BAÑO",DeviceZone.parse("baño"));assertThrows(DomainException.class,()->DeviceZone.parse("custom"));
 }
 @Test void cannotRemoveGeneralWhileZoneMetersRemain(){
  var general=general();var zone=new Device(UUID.randomUUID(),general.unitId(),"z","Cocina","COCINA",DeviceState.active,Instant.now());
  assertThrows(DomainException.class,()->DeviceZonePolicy.validate(List.of(general,zone),general.id(),"GENERAL",DeviceState.disabled));
 }
 @Test void legacyDuplicateGeneralsCanBeCorrectedExplicitly(){
  var first=general();var second=new Device(UUID.randomUUID(),first.unitId(),"second","Otro general","GENERAL",DeviceState.active,Instant.now());
  assertDoesNotThrow(()->DeviceZonePolicy.validate(List.of(first,second),second.id(),"COCINA",DeviceState.active));
  assertDoesNotThrow(()->DeviceZonePolicy.validate(List.of(first,second),second.id(),"GENERAL",DeviceState.disabled));
 }
}
