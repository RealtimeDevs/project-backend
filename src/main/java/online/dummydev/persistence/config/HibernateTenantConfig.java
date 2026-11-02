
package online.dummydev.persistence.config;

import org.hibernate.cfg.MultiTenancySettings;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateTenantConfig {

    @Bean
    HibernatePropertiesCustomizer tenantResolverCustomizer(
            HibernateTenantResolver resolver) {

        return properties -> properties.put(
                MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER,
                resolver
        );
    }
}
