package com.hydrosmart.properties.interfaces.rest.transform;
import org.springframework.security.oauth2.jwt.Jwt;
import com.hydrosmart.properties.domain.model.valueobjects.Actor;
public final class ActorFromJwtAssembler {
    private ActorFromJwtAssembler() {}
    public static Actor toActor(Jwt jwt) { return new Actor(jwt.getSubject(),jwt.getClaimAsString("profile_type")); }
}
