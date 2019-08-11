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

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * @author maximen39
 */
@Getter
@Setter
public class ChangeForm {

    @NotBlank(message = "{change.oldPassword.notblank}")
    private String oldPassword;

    @NotNull(message = "{change.password.notnull}")
    @Size(min = 6, max = 32, message = "{change.password.size}")
    @NotBlank(message = "{change.password.notblank}")
    private String password;

    private String repeatPassword;

    @AssertTrue(message = "{change.password.compare}")
    private boolean isValid() {
        return Objects.equals(password, repeatPassword) && isPasswordsEquals();
    }

    @AssertTrue(message = "{change.password.equals}")
    private boolean isPasswordsEquals() {
        return oldPassword != null && password != null &&
                !oldPassword.equalsIgnoreCase(password) &&
                !oldPassword.toLowerCase().contains(password.toLowerCase()) &&
                !password.toLowerCase().contains(oldPassword.toLowerCase());
    }
}
