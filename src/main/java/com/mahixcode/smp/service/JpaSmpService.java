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

import com.mahixcode.smp.config.CachingConfig;
import com.mahixcode.smp.dao.*;
import com.mahixcode.smp.exception.CurrencyNotFoundException;
import com.mahixcode.smp.model.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author maximen39
 */
@Service
@Transactional(readOnly = true)
public class JpaSmpService implements SmpService {

    private final CurrencyDao currencyDao;
    private final AccountDao accountDao;
    private final HistoryDao historyDao;
    private final WalletDao walletDao;
    private final ServerDao serverDao;
    private final GoodsDao goodsDao;
    private final GroupDao groupDao;
    private final BanDao banDao;

    public JpaSmpService(AccountDao accountDao, HistoryDao historyDao, GroupDao groupDao, BanDao banDao,
                         ServerDao serverDao, WalletDao walletDao, CurrencyDao currencyDao, GoodsDao goodsDao) {
        this.accountDao = accountDao;
        this.historyDao = historyDao;
        this.groupDao = groupDao;
        this.banDao = banDao;
        this.serverDao = serverDao;
        this.walletDao = walletDao;
        this.currencyDao = currencyDao;
        this.goodsDao = goodsDao;
    }

    @Override
    public Optional<AccountModel> getAccount(long id) {
        return accountDao.findById(id);
    }

    @Override
    public Optional<AccountModel> getAccountByName(String username) {
        return accountDao.findByUsername(username);
    }

    @Override
    public Optional<AccountModel> getAccountByEmail(String email) {
        return accountDao.findByEmail(email);
    }

    @Override
    public Collection<GroupModel> getGroupsByAccount(AccountModel account) {
        return groupDao.findGroupsByAccount(account);
    }

    @Override
    public Collection<WalletModel> getWallets(long id) {
        return walletDao.findAllByAccount_Id(id);
    }

    @Override
    public boolean hasAccountByName(String name) {
        return getAccountByName(name).isPresent();
    }

    @Override
    public boolean hasAccountByEmail(String email) {
        return getAccountByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public long saveHistory(HistoryModel history) {
        return historyDao.save(history).getId();
    }

    @Override
    @Transactional
    public long saveAccount(AccountModel account) {
        return accountDao.save(account).getId();
    }

    @Transactional
    @Override
    public long saveWallet(WalletModel walletModel) {
        return walletDao.save(walletModel).getId();
    }

    @Override
    @Transactional
    public void updatePassword(long id, String password) {
        accountDao.updatePassword(id, password);
    }

    @Override
    @Cacheable(value = {CachingConfig.BANS})
    public List<BanModel> getBans() {
        return banDao.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public List<CurrencyModel> getCurrencies() {
        return currencyDao.findAll();
    }

    @Override
    public List<GoodsModel> getGoods() {
        return goodsDao.findAll();
    }

    @Override
    public CurrencyModel getCurrencyById(long id) {
        return getCurrencies()
                .stream()
                .filter(currencyModel -> currencyModel.getId() == id)
                .findFirst().orElseThrow(() -> new CurrencyNotFoundException("Валюта не найдена!"));
    }

    @Override
    @Cacheable(value = {CachingConfig.ACCOUNTS}, key = "#id")
    public Optional<String> getNameByAccountId(long id) {
        return accountDao.findById(id).flatMap(accountModel -> Optional.of(accountModel.getUsername()));
    }

    @Override
    @Cacheable(value = {CachingConfig.SERVERS}, key = "#id")
    public Optional<String> getNameByServerId(long id) {
        return serverDao.findById(id).flatMap(serverModel -> Optional.of(serverModel.getName()));
    }
}
