
package online.dummydev.config;

import online.dummydev.infrastructure.security.AppJwtAuthenticationConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final AppJwtAuthenticationConverter converter;

    public SecurityConfig(
            AppJwtAuthenticationConverter converter) {

        this.converter = converter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http) {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(converter)
                        )
                );

        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
            String jwksUrl,

            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
            String issuer,

            @Value("${app.security.authorized-party}")
            String authorizedParty) {

        NimbusJwtDecoder decoder =
                NimbusJwtDecoder.withJwkSetUri(jwksUrl).build();

        OAuth2TokenValidator<Jwt> defaultValidator =
                JwtValidators.createDefaultWithIssuer(issuer);

        OAuth2TokenValidator<Jwt> authorizedPartyValidator = jwt -> {
            String azp = jwt.getClaimAsString("azp");

            if (azp == null || authorizedParty.equals(azp)) {
                return OAuth2TokenValidatorResult.success();
            }

            return OAuth2TokenValidatorResult.failure(
                    new OAuth2Error(
                            "invalid_token",
                            "Unauthorized token origin",
                            null
                    )
            );
        };

        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(
                        defaultValidator,
                        authorizedPartyValidator
                )
        );

        return decoder;
    }
}
