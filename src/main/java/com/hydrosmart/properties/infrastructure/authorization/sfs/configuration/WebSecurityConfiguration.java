package com.hydrosmart.properties.infrastructure.authorization.sfs.configuration;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import com.hydrosmart.properties.infrastructure.authorization.sfs.pipeline.InternalServiceAuthenticationFilter;
@Configuration
public class WebSecurityConfiguration {
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http,@Value("${INTERNAL_SERVICE_TOKEN}") String token) throws Exception {
        return http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a->a
                .dispatcherTypeMatchers(jakarta.servlet.DispatcherType.ERROR).permitAll()
                .requestMatchers(HttpMethod.GET,"/actuator/health","/actuator/health/**").permitAll()
                .requestMatchers("/internal/v1/units/*/access").authenticated()
                .requestMatchers("/internal/v1/**").hasRole("SERVICE")
                .anyRequest().authenticated())
            .addFilterBefore(new InternalServiceAuthenticationFilter(token),BearerTokenAuthenticationFilter.class)
            .addFilterBefore(new com.hydrosmart.properties.infrastructure.authorization.AccountLifecycle.Filter(),org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter.class)
            .addFilterBefore(new com.hydrosmart.properties.shared.infrastructure.messaging.RequestTrace.Filter(),org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter.class)
            .oauth2ResourceServer(o->o.jwt(Customizer.withDefaults())).build();
    }
}
