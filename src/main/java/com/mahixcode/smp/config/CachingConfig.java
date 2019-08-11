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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;

/**
 * @author maximen39
 */
@Configuration
@EnableCaching
public class CachingConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CachingConfig.class);

    public static final String BANS = "BANS";
    public static final String ACCOUNTS = "ACCOUNTS";
    public static final String SERVERS = "SERVERS";

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache(BANS),
                new ConcurrentMapCache(ACCOUNTS),
                new ConcurrentMapCache(SERVERS)
        ));
        return cacheManager;
    }

    @CacheEvict(allEntries = true, value = {BANS})
    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 500)
    public void bansCacheEvict() {
        LOGGER.info("Flush Cache for BANS");
    }
}
