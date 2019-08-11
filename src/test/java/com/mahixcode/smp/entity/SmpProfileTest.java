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
import com.mahixcode.smp.exception.WalletNotFoundException;
import com.mahixcode.smp.model.CurrencyModel;
import com.mahixcode.smp.model.WalletModel;
import com.mahixcode.smp.service.SmpService;
import com.mahixcode.smp.store.SmpHashSet;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author maximen39
 */
public class SmpProfileTest {

    private SmpProfile smpProfile;
    private SmpService smpService;
    private long id;

    @Before
    public void before() {
        String empty = "";
        smpProfile = new SmpProfile(empty, empty, empty);
        smpProfile.wallets(new SmpHashSet<>());
        smpService = mock(SmpService.class);
        when(smpService.saveWallet(any())).thenAnswer(invocationOnMock -> {
            WalletModel walletModel = invocationOnMock.getArgument(0);
            if (walletModel.getId() != 0) {
                return walletModel.getId();
            } else {
                return ++id;
            }
        });

        when(smpService.getCurrencyById(anyLong()))
                .thenAnswer(invocationOnMock ->
                        new CurrencyModel().setId(invocationOnMock.getArgument(0))
                );

        smpProfile.initSmpService(smpService);
    }

    @Test
    public void depositWithdrawExchangeTest() {
        smpProfile.deposit(1, BigDecimal.valueOf(77.48));
        smpProfile.deposit(2, BigDecimal.valueOf(1789));

        smpProfile.withdraw(1, BigDecimal.valueOf(20));
        smpProfile.withdraw(2, BigDecimal.valueOf(999.22));


        Config.ExchangeCurrency exchangeCurrency = new Config.ExchangeCurrency();
        exchangeCurrency.exchangeRate = BigDecimal.valueOf(2.5);
        exchangeCurrency.fromCurrency = 1;
        exchangeCurrency.toCurrency = 2;
        Config config = new Config();
        config.exchangeCurrencies = Collections.singleton(exchangeCurrency);

        smpProfile.exchange(config, 1, 2, BigDecimal.valueOf(15.86));

        assertEquals(smpProfile.wallet(1).getBalance(), BigDecimal.valueOf(41.62));
        assertEquals(smpProfile.wallet(2).getBalance(), BigDecimal.valueOf(829.43));
    }

    @Test(expected = WalletNotFoundException.class)
    public void notFoundWithdrawValueTest() {
        smpProfile.wallets().clear();

        smpProfile.withdraw(1, BigDecimal.valueOf(54));

        assertEquals(smpProfile.wallets().size(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidDepositValueTest() {
        smpProfile.deposit(1, BigDecimal.valueOf(-77.48));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidWithdrawValueTest() {
        smpProfile.withdraw(1, BigDecimal.valueOf(-77.48));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidExchangeValueTest() {
        smpProfile.exchange(new Config(), 1, 2, BigDecimal.valueOf(-77.48));
    }

    @Test(expected = ExchangeException.class)
    public void exchangeExceptionTest() {
        smpProfile.exchange(new Config(), 1, 2, BigDecimal.valueOf(5874.48));
    }
}
