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
package com.mahixcode.smp.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;

/**
 * @author maximen39
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<O> {

    private long timestamp;
    private O result;
    private String message;

    public Result(O result, String message) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.result = result;
        this.message = message;
    }

    public Result(O result) {
        this(result, null);
    }

    public static <O> Result<O> of(O object) {
        return new Result<>(object);
    }

    public static Result success(String message) {
        return new Result<>(singletonMap("success", message));
    }

    public static Result error(String message) {
        return new Result<>(null, message);
    }

    public static Result validErrors(List<Pair<String, String>> errors) {
        return new Result<>(singletonMap("validationErrors", errors));
    }

    public static Result singletonValidError(String field, String message) {
        return new Result<>(singletonMap("validationErrors", singletonList(Pair.of(field, message))));
    }
}

