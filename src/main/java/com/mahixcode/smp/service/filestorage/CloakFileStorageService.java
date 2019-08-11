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
package com.mahixcode.smp.service.filestorage;

import com.mahixcode.smp.TextureType;
import com.mahixcode.smp.config.json.Config;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Paths;

/**
 * @author maximen39
 */
@Component
public class CloakFileStorageService extends BaseFileStorageService {

    public CloakFileStorageService(Config config) throws IOException {
        super(Paths.get(String.format(config.textureFolder, TextureType.CLOAK.dir())));
    }

    @Override
    public void store(InputStream inputStream, String name) throws IOException {
        if (!name.endsWith(".png")) {
            throw new FileSystemException(name, null, "File must be in PNG format!");
        }

        byte[] bytes = IOUtils.toByteArray(inputStream);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        if (bufferedImage.getWidth() % 64 != 0 || bufferedImage.getWidth() > 1024 ||
                bufferedImage.getHeight() % 32 != 0 || bufferedImage.getWidth() > 512) {
            throw new FileSystemException(name, null, "Размер плаща должен быть между 64x32 и 1024x512!");
        }

        super.store(new ByteArrayInputStream(bytes), name);
    }
}
