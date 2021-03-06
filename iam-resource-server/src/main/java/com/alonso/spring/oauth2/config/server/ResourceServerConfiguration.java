package com.alonso.spring.oauth2.config.server;

import com.alonso.spring.oauth2.config.tokenservice.TokenServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableResourceServer
@Import(TokenServiceConfig.class)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "iam-resource-server-rest-api";
    private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('iam.read')";
    private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('iam.write')";
    private static final String SECURED_PATTERN_USER = "/v1/users/**";
    private static final String SECURED_PATTERN_AUTHORITY = "/v1/authorities/**";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers(SECURED_PATTERN_USER, SECURED_PATTERN_AUTHORITY)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, SECURED_PATTERN_USER).access(SECURED_WRITE_SCOPE)
                .antMatchers(HttpMethod.PUT, SECURED_PATTERN_USER).access(SECURED_WRITE_SCOPE)
                .antMatchers(HttpMethod.DELETE, SECURED_PATTERN_USER).access(SECURED_WRITE_SCOPE)
                .anyRequest().access(SECURED_READ_SCOPE)
                .and().httpBasic()
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
