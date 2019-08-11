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
import com.mahixcode.smp.config.json.Config;
import com.mahixcode.smp.model.CurrencyModel;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author maximen39
 */
@JsonComponent
public class CurrencyJsonSerialize extends JsonSerializer<CurrencyModel> {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private final Config config;

    public CurrencyJsonSerialize(Config config) {
        this.config = config;
    }

    @Override
    public void serialize(CurrencyModel currencyModel, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", currencyModel.getId());
        jsonGenerator.writeStringField("name", currencyModel.getName());
        jsonGenerator.writeStringField("shortName", currencyModel.getShortName());
        jsonGenerator.writeStringField("pluralName", currencyModel.getPluralName());

        List<Config.ExchangeCurrency> canExchangeTo = config.exchangeCurrencies
                .stream()
                .filter(exchangeCurrency ->
                        exchangeCurrency.fromCurrency == currencyModel.getId()
                ).collect(Collectors.toList());

        jsonGenerator.writeObjectField("canExchangeTo", canExchangeTo);

        jsonGenerator.writeStringField("createdAt", DATE_FORMAT.format(currencyModel.getCreatedAt().getTime()));
        jsonGenerator.writeEndObject();
    }
}
