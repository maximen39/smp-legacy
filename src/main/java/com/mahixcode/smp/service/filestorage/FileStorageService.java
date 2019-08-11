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

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author maximen39
 */
public interface FileStorageService {

    void store(MultipartFile multipartFile, String name) throws IOException;

    void store(byte[] bytes, String name) throws IOException;

    void store(InputStream inputStream, String name) throws IOException;

    Stream<Path> loadAll() throws IOException;

    Path load(String name);

    Path storagePath();

    void clear() throws IOException;

    boolean delete(String name) throws IOException;
}
