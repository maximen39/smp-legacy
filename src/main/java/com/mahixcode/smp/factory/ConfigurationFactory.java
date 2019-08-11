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
package com.mahixcode.smp.factory;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author maximen39
 */
public class ConfigurationFactory {

    private final File dataFolder;
    private final Gson configurationGson;

    public ConfigurationFactory(File dataFolder, Gson configurationGson) {
        this.dataFolder = dataFolder;
        this.configurationGson = configurationGson;
    }

    public Object loadJson(Class<?> clazz, String name) {
        File file = new File(dataFolder + "/" + name);
        try (FileReader fileReader = new FileReader(file)) {
            return configurationGson.fromJson(fileReader, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveJson(Object o, String name) {
        File file = new File(dataFolder + "/" + name);
        if (o != null) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                String json = configurationGson.toJson(o, o.getClass());
                fileWriter.write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Object loadDefaultJson(Class<?> clazz, String name)
            throws IllegalAccessException, InstantiationException, IOException {
        Object jsonConfig;
        File file = new File(dataFolder + "/" + name);

        if (file.exists()) {
            jsonConfig = loadJson(clazz, name);
            saveJson(jsonConfig, name); //add new fields if такие exists
        } else {
            file.createNewFile();
            jsonConfig = clazz.newInstance();
            saveJson(jsonConfig, name);
        }
        return jsonConfig;
    }
}
