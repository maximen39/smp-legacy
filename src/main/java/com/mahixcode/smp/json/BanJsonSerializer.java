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
import com.mahixcode.smp.model.BanModel;
import com.mahixcode.smp.service.SmpService;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author maximen39
 */
@JsonComponent
public class BanJsonSerializer extends JsonSerializer<BanModel> {
    private static final String UNKNOWN = "unknown";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private final SmpService smpService;

    public BanJsonSerializer(SmpService smpService) {
        this.smpService = smpService;
    }

    @Override
    public void serialize(BanModel banModel, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", banModel.getId());
        jsonGenerator.writeStringField("account", smpService
                .getNameByAccountId(banModel
                        .getAccount()
                        .getId()
                ).orElse(UNKNOWN)
        );
        jsonGenerator.writeStringField("server", smpService
                .getNameByServerId(banModel
                        .getServer()
                        .getId()
                ).orElse(UNKNOWN));
        jsonGenerator.writeStringField("performer", smpService
                .getNameByAccountId(banModel
                        .getAccount()
                        .getId()
                ).orElse(UNKNOWN));
        jsonGenerator.writeStringField("reason", banModel.getReason());
        jsonGenerator.writeStringField("endsAt", DATE_FORMAT.format(banModel.getEndsAt().getTime()));
        jsonGenerator.writeStringField("updatedAt", DATE_FORMAT.format(banModel.getUpdatedAt().getTime()));
        jsonGenerator.writeStringField("createdAt", DATE_FORMAT.format(banModel.getCreatedAt().getTime()));
        jsonGenerator.writeEndObject();
    }
}
