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

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author maximen39
 */
public class SmpProfileSync extends SmpProfile {
    private ReentrantLock lock;

    public SmpProfileSync(String username, String email, String password) {
        super(username, email, password);
        this.lock = new ReentrantLock();
    }

    public boolean isLock() {
        return lock.isLocked();
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    @Override
    public BigDecimal deposit(long currencyId, BigDecimal value) {
        try {
            lock();
            return super.deposit(currencyId, value);
        } finally {
            unlock();
        }
    }

    @Override
    public BigDecimal withdraw(long currencyId, BigDecimal value) {
        try {
            lock();
            return super.withdraw(currencyId, value);
        } finally {
            unlock();
        }
    }
}
