package com.hydrosmart.properties.application;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.hydrosmart.properties.domain.services.AssetRepository;
import com.hydrosmart.properties.domain.model.aggregates.Property;
import com.hydrosmart.properties.domain.model.commands.*;
import com.hydrosmart.properties.domain.model.valueobjects.*;
import com.hydrosmart.properties.application.internal.queryservices.ResourceAccessPolicy;
import com.hydrosmart.properties.application.internal.commandservices.PropertyCommandServiceImpl;

class ResourceIsolationTest {
    @Test void tenantCannotCreateProperty() {
        var repository=mock(AssetRepository.class);
        var service=new PropertyCommandServiceImpl(repository,new ResourceAccessPolicy(repository));
        var error=assertThrows(DomainException.class,()->service.handle(
            new CreatePropertyCommand(new Actor("tenant","inquilino"),"Home","Address")));
        assertEquals(DomainException.Kind.FORBIDDEN,error.kind());
        verifyNoInteractions(repository);
    }
    @Test void anotherOwnerCannotModifyProperty() {
        var repository=mock(AssetRepository.class);
        var id=UUID.randomUUID();
        when(repository.property(id)).thenReturn(Optional.of(new Property(id,"owner-a","Home","Address",Instant.now())));
        var service=new PropertyCommandServiceImpl(repository,new ResourceAccessPolicy(repository));
        var error=assertThrows(DomainException.class,()->service.handle(
            new UpdatePropertyCommand(new Actor("owner-b","propietario"),id,"Changed","Address")));
        assertEquals(DomainException.Kind.NOT_FOUND,error.kind());
        verify(repository,never()).update(any(Property.class));
    }
    @Test void malformedIdentityAndEmptyNamesAreRejected() {
        assertThrows(DomainException.class,()->new Actor("user",null));
        assertThrows(DomainException.class,()->new Property(UUID.randomUUID(),"owner"," ","Address",Instant.now()));
        assertThrows(DomainException.class,()->new Property(new UUID(0,0),"owner","Home","Address",Instant.now()));
    }
}

