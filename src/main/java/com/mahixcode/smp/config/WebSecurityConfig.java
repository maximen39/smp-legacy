/*
 * Copyright (C) 2018 maximen39
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mahixcode.smp.config;

import com.mahixcode.smp.config.json.Config;
import com.mahixcode.smp.encryption.PasswordEncoderDecorator;
import com.mahixcode.smp.handler.AuthSuccessHandler;
import com.mahixcode.smp.handler.SmpAuthenticationEntryPoint;
import com.mahixcode.smp.provider.SmpDaoAuthenticationProvider;
import com.mahixcode.smp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

/**
 * @author maximen39
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //TODO: /test/** and mappings remove in release!
    private static final String[] PERMIT_ALL = {
            "/error", "/launcher/**", "/auth/registration",
            "/minecraftButton", "/texture/**", "/auth/recovery", "/ban/list", "/test/**"};
    private static final String[] ONLY_ADMIN = {"/admin/**"};
    private static final String[] ONLY_AUTH = {"/**"};

    private final Config config;
    private final AuthSuccessHandler authSuccessHandler;
    private final UserDetailsServiceImpl userDetailsService;
    private final SmpAuthenticationEntryPoint smpAuthenticationEntryPoint;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthSuccessHandler authSuccessHandler,
                             SmpAuthenticationEntryPoint smpAuthenticationEntryPoint, Config config) {
        this.userDetailsService = userDetailsService;
        this.authSuccessHandler = authSuccessHandler;
        this.smpAuthenticationEntryPoint = smpAuthenticationEntryPoint;
        this.config = config;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoderDecorator();
    }

    @Bean
    public SmpDaoAuthenticationProvider smpAuthProvider() throws Exception {
        SmpDaoAuthenticationProvider provider = new SmpDaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(smpAuthProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(PERMIT_ALL).permitAll();
        http.authorizeRequests().antMatchers(ONLY_ADMIN).hasAuthority(config.springReservedAdminGroup);
        http.authorizeRequests().antMatchers(ONLY_AUTH).authenticated();
        http
                .csrf().disable()
                .cors()
                .configurationSource(httpServletRequest -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setAllowedOrigins(config.corsAllowedOrigins);
                    corsConfiguration.setAllowedHeaders(config.corsAllowedHeaders);
                    corsConfiguration.setAllowedMethods(config.corsAllowedMethods);
                    return corsConfiguration;
                })
                .and()
                .httpBasic()
                .authenticationEntryPoint(smpAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .maximumSessions(1);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new InMemoryTokenRepositoryImpl();
    }
}
