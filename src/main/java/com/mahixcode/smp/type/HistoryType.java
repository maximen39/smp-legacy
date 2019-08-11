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
public enum HistoryType {
    AUTHORIZATION_SITE("authorization_site"),
    AUTHORIZATION_LAUNCHER("authorization_launcher"),
    REGISTRATION("registration"),
    RECOVERY_PASSWORD("recovery_password"),
    CHANGE_PASSWORD("change_password");

    private final String historyType;

    HistoryType(String historyType) {
        this.historyType = historyType;
    }

    public String historyType() {
        return historyType;
    }

    public static HistoryType fromString(String historyTypeName) {
        for (HistoryType historyType : values()) {
            if (historyType.historyType().equals(historyTypeName))
                return historyType;
        }
        return null;
    }
}
