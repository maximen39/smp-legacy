/*
 * Copyright (C) 2019 maximen39
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

import com.mahixcode.smp.entity.SmpProfile;
import com.mahixcode.smp.service.SmpService;
import com.mahixcode.smp.storage.SmpDatabaseProfileStore;
import com.mahixcode.smp.storage.SmpInMemoryProfileStoreCache;
import com.mahixcode.smp.store.CaseInsensitiveMap;
import com.mahixcode.smp.store.Store;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Optional;
import java.util.concurrent.Executors;

/**
 * @author maximen39
 */
@Configuration
@EnableScheduling
public class SpringConfig {

    private final SmpService smpService;

    public SpringConfig(SmpService smpService) {
        this.smpService = smpService;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());
    }

    @Bean
    public Store<String, Optional<SmpProfile>> profileStore() {
        return new SmpInMemoryProfileStoreCache(
                new CaseInsensitiveMap<>(),
                new SmpDatabaseProfileStore(smpService)
        );
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("file:./localization/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}