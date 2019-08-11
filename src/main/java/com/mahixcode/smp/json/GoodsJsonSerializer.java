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
package com.mahixcode.smp.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mahixcode.smp.model.GoodsModel;
import com.mahixcode.smp.service.filestorage.BaseFileStorageService;
import com.mahixcode.smp.service.filestorage.BaseFileStorageServiceImpl;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;

/**
 * @author maximen39
 */
@JsonComponent
public class GoodsJsonSerializer extends JsonSerializer<GoodsModel> {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private BaseFileStorageService baseFileStorageService;

    public GoodsJsonSerializer() throws IOException {
        this.baseFileStorageService = new BaseFileStorageServiceImpl(Paths.get("./images/goods/"));
    }

    @Override
    public void serialize(GoodsModel goodsModel, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        Path path = baseFileStorageService.load(goodsModel.getId() + ".png");
        if (Files.exists(path)) {
            byte[] bytes = Files.readAllBytes(path);
            jsonGenerator.writeStringField("icon", new String(Base64.getEncoder().encode(bytes)));
        } else {
            jsonGenerator.writeStringField("icon", null);
        }

        jsonGenerator.writeNumberField("id", goodsModel.getId());
        jsonGenerator.writeStringField("name", goodsModel.getName());
        jsonGenerator.writeStringField("description", goodsModel.getDescription());
        jsonGenerator.writeObjectField("currency", goodsModel.getCurrency());
        jsonGenerator.writeObjectField("promocode", goodsModel.getPromocode());
        jsonGenerator.writeNumberField("price", goodsModel.getPrice());
        jsonGenerator.writeNumberField("newPrice", goodsModel.getNewPrice());
        jsonGenerator.writeNumberField("type", goodsModel.getType());
        jsonGenerator.writeStringField("updatedAt", DATE_FORMAT.format(goodsModel.getUpdatedAt().getTime()));
        jsonGenerator.writeStringField("createdAt", DATE_FORMAT.format(goodsModel.getCreatedAt().getTime()));

        jsonGenerator.writeEndObject();
    }
}
