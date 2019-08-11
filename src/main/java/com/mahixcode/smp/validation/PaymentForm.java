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
import java.math.BigDecimal;

/**
 * @author maximen39
 */
@Getter
@Setter
public class PaymentForm {

    @NotNull(message = "{payment.name.notnull}")
    @Size(min = 3, max = 16, message = "{payment.name.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{payment.name.pattern}")
    private String name;


    @NotNull(message = "{payment.deposit.notnull}")
    @Digits(integer = 5, fraction = 2, message = "{payment.deposit.digits}")
    @DecimalMin(value = "1.00", message = "{payment.deposit.decimalmin}")
    @DecimalMax(value = "10000.00", message = "{payment.deposit.decimalmax}")
    private BigDecimal deposit;
}
