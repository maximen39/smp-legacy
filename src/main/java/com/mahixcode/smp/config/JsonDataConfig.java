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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mahixcode.smp.config.json.Config;
import com.mahixcode.smp.factory.ConfigurationFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

/**
 * @author maximen39
 */
@Configuration
public class JsonDataConfig {

    @Bean
    public Gson configurationGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    @Bean
    public File dataFolder() {
        File file = new File("./");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    @Bean
    public ConfigurationFactory configurationFactory() {
        return new ConfigurationFactory(dataFolder(), configurationGson());
    }

    @Bean
    public Config config() {
        try {
            return (Config) configurationFactory().loadDefaultJson(Config.class, Config.NAME);
        } catch (IllegalAccessException | InstantiationException | IOException e) {
            return new Config();
        }
    }
}
