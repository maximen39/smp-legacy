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
package com.mahixcode.smp.config.json;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author maximen39
 */
public class Config {
    public static final String NAME = "configuration.json";

    public String smp = "Servers Management Project";

    public String domain = "unimc.ru";
    public String protocol = "https";

    public String springReservedAdminGroup = "spring_admin";
    public String launcherReservedAdminGroup = "launcher_admin";
    public String launcherReservedServerGroup = "launcher_server";

    public List<String> corsAllowedOrigins = Arrays.asList("https://unimc.ru", "https://www.unimc.ru");
    public List<String> corsAllowedHeaders = Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Request-With", "Cookie", "Set-Cookie");
    public List<String> corsAllowedMethods = Arrays.asList("POST", "GET", "OPTIONS", "PUT", "DELETE");

    public String emailUsername = "recovery@unimc.ru";
    public String emailPassword = "AosijhdoisajhdoiTiLohjsad009uas0d";
    public String emailName = "UniMC";
    public String emailHost = "smtp.yandex.ru";
    public int emailPort = 465;

    public String dataSourceDriver = "com.mysql.cj.jdbc.Driver";
    public String dataSourceUrl = "jdbc:mysql://164.132.233.12:3306/dev?serverTimezone=UTC";
    public String dataSourceUsername = "dev";
    public String dataSourcePassword = "hJXdalDpaKd0UdKz";

    public boolean hibernateShowSql = true;
    public String hibernateDDL = "update";
    public String hibernateDialect = "org.hibernate.dialect.MySQL57Dialect";

    public String textureFolder = "./texture/%s/";

    public Collection<ExchangeCurrency> exchangeCurrencies = Collections.singleton(new ExchangeCurrency());

    public static class ExchangeCurrency {
        public int fromCurrency;
        public int toCurrency;
        public BigDecimal exchangeRate;
    }
}
