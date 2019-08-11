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
package com.mahixcode.smp.profile;

import static java.util.Objects.requireNonNull;

/**
 * @author maximen39
 */
public abstract class BaseProfile implements Profile {
    private long id;
    private String username;
    private String email;

    public BaseProfile(String username, String email) {
        this.username = requireNonNull(username, "username must be not null");
        this.email = email;
    }

    @Override
    public long id() {
        return id;
    }

    public BaseProfile id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String username() {
        return username;
    }

    public BaseProfile username(String username) {
        this.username = requireNonNull(username, "username must be not null");
        return this;
    }

    @Override
    public String email() {
        return email;
    }

    public BaseProfile email(String email) {
        this.email = requireNonNull(email, "email must be not null");
        return this;
    }
}
