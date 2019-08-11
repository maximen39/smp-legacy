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
package com.mahixcode.smp.entity;


import com.mahixcode.smp.config.json.Config;
import com.mahixcode.smp.exception.ExchangeException;
import com.mahixcode.smp.exception.NotEnoughException;
import com.mahixcode.smp.exception.WalletNotFoundException;
import com.mahixcode.smp.model.AccountModel;
import com.mahixcode.smp.model.GroupModel;
import com.mahixcode.smp.model.WalletModel;
import com.mahixcode.smp.profile.BaseProfile;
import com.mahixcode.smp.service.SmpService;
import com.mahixcode.smp.store.SmpHashSet;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * @author maximen39
 */
@Getter
@Setter
@Accessors(fluent = true)
public class SmpProfile extends BaseProfile {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.##");

    private SmpHashSet<WalletModel> wallets;
    private String password;
    private Collection<GroupModel> groups;
    private SessionUser sessionUser;
    private SmpService smpService;

    public SmpProfile(String username, String email, String password) {
        super(username, email);
        this.password = requireNonNull(password, "password must be not null");
    }

    public SmpProfile initSmpService(SmpService smpService) {
        this.smpService = smpService;
        return this;
    }

    public SmpHashSet<WalletModel> wallets() {
        if (wallets == null) {
            wallets = new SmpHashSet<>(smpService.getWallets(id()));
        }
        return wallets;
    }

    public List<WalletModel> sortedWallets() {
        Comparator<WalletModel> walletModelComparator = Comparator
                .comparingLong(WalletModel::getId);
        return wallets()
                .stream()
                .sorted(walletModelComparator)
                .collect(Collectors.toList());
    }

    public WalletModel wallet(long id) {
        return wallets()
                .stream()
                .filter(walletModel ->
                        walletModel.getId() == id
                ).findFirst()
                .orElse(null);
    }

    public boolean hasGroup(String group) {
        return groups.stream().anyMatch(groupModel ->
                groupModel.getName().equalsIgnoreCase(group)
        );
    }

    public Optional<GroupModel> maxGroup() {
        Comparator<GroupModel> groupModelComparator = Comparator
                .comparingLong(GroupModel::getPriority);
        return groups.stream().max(groupModelComparator);
    }

    public BigDecimal exchange(Config config, long from, long to, BigDecimal count) {
        if (count.doubleValue() <= 0) {
            throw new IllegalArgumentException("Invalid rate multiply value: " + count);
        }

        Config.ExchangeCurrency exchangeConfig = config.exchangeCurrencies.stream().filter(exchangeCurrency ->
                exchangeCurrency.fromCurrency == from &&
                        exchangeCurrency.toCurrency == to
        ).findFirst().orElseThrow(() ->
                new ExchangeException("Вы не можете обменять эту валюту " +
                        "[" + from + " => " + to + "]")
        );
        BigDecimal rate = exchangeConfig.exchangeRate;

        BigDecimal multiply = count.multiply(rate);
        if (multiply.doubleValue() < 1) {
            throw new ExchangeException(String.format("Минимальный обмен: %s к %s",
                    DECIMAL_FORMAT.format(BigDecimal.ONE.divide(rate, 2, BigDecimal.ROUND_HALF_UP)),
                    DECIMAL_FORMAT.format(1)
            ));
        }

        withdraw(from, count);
        deposit(to, multiply);

        return count;
    }

    public BigDecimal deposit(long currencyId, BigDecimal value) {
        if (value.doubleValue() <= 0) {
            throw new IllegalArgumentException("Invalid deposit value: " + value);
        }

        WalletModel wallet = wallets().stream()
                .filter(walletModel -> walletModel
                        .getCurrency().getId() == currencyId
                ).findFirst()
                .orElse(new WalletModel()
                        .setAccount(new AccountModel().setId(id()))
                        .setBalance(new BigDecimal(0))
                        .setCurrency(smpService.getCurrencyById(currencyId))
                );

        wallet.setBalance(wallet.getBalance().add(value).setScale(2, BigDecimal.ROUND_HALF_UP));

        long id = smpService.saveWallet(wallet);
        if (id <= 0) {
            throw new IllegalStateException("Invalid returned id!");
        }
        wallets.set(wallet.setId(id));

        return wallet.getBalance();
    }

    public BigDecimal withdraw(long currencyId, BigDecimal value) {
        if (value.doubleValue() <= 0) {
            throw new IllegalArgumentException("Invalid withdraw value: " + value);
        }

        WalletModel wallet = wallets().stream()
                .filter(walletModel -> walletModel
                        .getCurrency().getId() == currencyId
                ).findFirst()
                .orElseThrow(() -> new WalletNotFoundException("Wallet[" + currencyId + "] not found!"));

        if (wallet.getBalance().doubleValue() < value.doubleValue()) {
            throw new NotEnoughException("Недостаточно средств!");
        }

        wallet.setBalance(wallet.getBalance().subtract(value).setScale(2, BigDecimal.ROUND_HALF_UP));

        long id = smpService.saveWallet(wallet);
        if (id <= 0) {
            throw new IllegalStateException("Invalid returned id!");
        }
        wallets.set(wallet.setId(id));

        return wallet.getBalance();
    }
}
