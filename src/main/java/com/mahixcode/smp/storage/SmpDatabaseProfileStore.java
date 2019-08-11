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
package com.mahixcode.smp.storage;

import com.mahixcode.smp.entity.SmpProfile;
import com.mahixcode.smp.entity.SmpProfileSync;
import com.mahixcode.smp.model.AccountModel;
import com.mahixcode.smp.model.GroupModel;
import com.mahixcode.smp.service.SmpService;
import com.mahixcode.smp.store.Store;

import java.util.Collection;
import java.util.Optional;

/**
 * @author maximen39
 */
public class SmpDatabaseProfileStore implements Store<String, Optional<SmpProfile>> {

    private SmpService smpService;

    public SmpDatabaseProfileStore(SmpService smpService) {
        this.smpService = smpService;
    }

    @Override
    public Optional<SmpProfile> fetch(String username) {
        Optional<AccountModel> optionalAccountModel = smpService.getAccountByName(username);
        return optionalAccountModel.flatMap(this::fillProfile);

    }

    private Optional<SmpProfile> fillProfile(AccountModel accountModel) {
        Optional<SmpProfile> profileOptional = fromAccountModel(accountModel);
        return profileOptional.flatMap(smpProfile -> {
            Collection<GroupModel> groups = smpService.getGroupsByAccount(accountModel);
            smpProfile
                    .groups(groups)
                    .initSmpService(smpService);
            return Optional.of(smpProfile);
        });
    }

    public static Optional<SmpProfile> fromAccountModel(AccountModel accountModel) {
        if (accountModel == null) {
            return Optional.empty();
        }

        SmpProfile smpProfile = new SmpProfileSync(
                accountModel.getUsername(),
                accountModel.getEmail(),
                accountModel.getPassword()
        );
        smpProfile.id(accountModel.getId());

        return Optional.of(smpProfile);
    }

    @Override
    public Optional<SmpProfile> store(String username, Optional<SmpProfile> optionalSmpProfile) {
        if (!optionalSmpProfile.isPresent()) {
            return Optional.empty();
        }
        SmpProfile smpProfile = optionalSmpProfile.get();
        try {
            if (smpProfile instanceof SmpProfileSync) {
                ((SmpProfileSync) smpProfile).lock();
            }
            AccountModel accountModel = new AccountModel()
                    .setUsername(smpProfile.username())
                    .setEmail(smpProfile.email())
                    .setOnline(false)
                    .setPassword(smpProfile.password());
            smpService.saveAccount(accountModel);
        } finally {
            if (smpProfile instanceof SmpProfileSync) {
                ((SmpProfileSync) smpProfile).unlock();
            }
        }
        return optionalSmpProfile;
    }

    @Override
    public Optional<SmpProfile> remove(String key) {
        return Optional.empty();
    }
}
