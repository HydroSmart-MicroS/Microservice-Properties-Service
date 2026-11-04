package com.hydrosmart.properties.application;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.hydrosmart.properties.domain.model.valueobjects.Rules;
import com.hydrosmart.properties.domain.model.valueobjects.DomainException;
class TextValidationTest {
 @Test void textIsTrimmedAndBoundedWithoutControlCharacters(){
  assertEquals("Casa de José",Rules.text(" Casa de José ",120,"name"));
  for(String invalid:new String[]{"   ","a".repeat(121),"Casa\u0000falsa","Casa\nsegunda"})assertThrows(DomainException.class,()->Rules.text(invalid,120,"name"));
 }
}
