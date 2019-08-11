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

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * @author maximen39
 */
public abstract class BaseFileStorageService implements FileStorageService {

    private Path storagePath;

    public BaseFileStorageService(Path storagePath) throws IOException {
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }
        this.storagePath = storagePath;
    }

    @Override
    public void store(MultipartFile multipartFile, String name) throws IOException {
        this.store(multipartFile.getInputStream(), name);
    }

    @Override
    public void store(byte[] bytes, String name) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        this.store(byteArrayInputStream, name);
    }

    @Override
    public void store(InputStream inputStream, String name) throws IOException {
        Path path = load(name);
        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        IOUtils.closeQuietly(inputStream);
    }

    @Override
    public Stream<Path> loadAll() throws IOException {
        return Files.list(storagePath());
    }

    @Override
    public Path load(String name) {
        File[] files = storagePath().toFile().listFiles(new CaseInsensitiveFilter(name));
        return files != null && files.length >= 1 ? files[0].toPath() : storagePath().resolve(name);
    }

    @Override
    public Path storagePath() {
        return this.storagePath;
    }

    @Override
    public void clear() throws IOException {
        Files.deleteIfExists(storagePath());
    }

    @Override
    public boolean delete(String name) throws IOException {
        Path path = load(name);
        return Files.deleteIfExists(path);
    }
}
