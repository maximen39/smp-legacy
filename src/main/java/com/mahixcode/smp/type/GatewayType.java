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
package com.mahixcode.smp.type;

/**
 * @author maximen39
 */
public enum GatewayType {
    FREEKASSA("freekassa"),
    INTERKASSA("interkassa"),
    QIWI("qiwi"),
    UNITPAY("unitpay"),
    ANYPAY("anypay");

    private final String gatewayType;

    GatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String gatewayType() {
        return gatewayType;
    }

    public static GatewayType fromString(String gatewayTypeName) {
        for (GatewayType gatewayType : values()) {
            if (gatewayType.gatewayType().equals(gatewayTypeName))
                return gatewayType;
        }
        return null;
    }
}
