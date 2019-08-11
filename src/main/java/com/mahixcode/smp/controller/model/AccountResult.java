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

import com.mahixcode.smp.model.AccountStatsModel;
import com.mahixcode.smp.model.BanModel;
import com.mahixcode.smp.model.WalletModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author maximen39
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountResult {

    private String email;
    private String group;
    private String username;
    private byte[] headImage;
    private byte[] skinImage;
    private byte[] cloakImage;
    private List<BanModel> bans;
    private List<WalletModel> wallets;
    private List<AccountStatsModel> stats;
}
