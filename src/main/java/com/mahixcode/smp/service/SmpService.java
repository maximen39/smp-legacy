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
package com.mahixcode.smp.service;

import com.mahixcode.smp.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author maximen39
 */
public interface SmpService {

    Optional<AccountModel> getAccount(long id);

    Optional<AccountModel> getAccountByName(String username);

    Optional<AccountModel> getAccountByEmail(String email);

    Collection<GroupModel> getGroupsByAccount(AccountModel account);

    Collection<WalletModel> getWallets(long id);

    boolean hasAccountByName(String name);

    boolean hasAccountByEmail(String email);

    long saveHistory(HistoryModel history);

    long saveAccount(AccountModel account);

    long saveWallet(WalletModel walletModel);

    void updatePassword(long id, String password);

    List<BanModel> getBans();

    List<CurrencyModel> getCurrencies();

    List<GoodsModel> getGoods();

    CurrencyModel getCurrencyById(long id);

    Optional<String> getNameByAccountId(long id);

    Optional<String> getNameByServerId(long id);
}
