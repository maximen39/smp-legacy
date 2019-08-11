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
package com.mahixcode.smp.validation;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Objects;

/**
 * @author maximen39
 */
@Getter
@Setter
public class RegistrationForm {

    @NotNull(message = "{registration.username.notnull}")
    @Size(min = 3, max = 16, message = "{registration.username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{registration.username.pattern}")
    private String username;

    @NotNull(message = "{registration.email.notnull}")
    @Size(min = 7, max = 254, message = "{registration.email.size}")
    @Pattern(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$", message = "{registration.email.pattern}")
    private String email;

    @NotNull(message = "{registration.password.notnull}")
    @Size(min = 6, max = 32, message = "{registration.password.size}")
    @NotBlank(message = "{registration.password.notblank}")
    private String password;

    private String repeatPassword;

    @AssertTrue(message = "{registration.password.compare}")
    private boolean isValid() {
        return Objects.equals(password, repeatPassword);
    }
}
