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
package com.mahixcode.smp.handler;

import com.mahixcode.smp.entity.SmpProfile;
import com.mahixcode.smp.model.AccountModel;
import com.mahixcode.smp.model.HistoryModel;
import com.mahixcode.smp.service.SmpService;
import com.mahixcode.smp.store.Store;
import com.mahixcode.smp.type.HistoryType;
import com.mahixcode.smp.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author maximen39
 */
@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private int authCount = 0;
    private SmpService smpService;
    private Store<String, Optional<SmpProfile>> profileStore;

    @Autowired
    public AuthSuccessHandler(SmpService smpService, Store<String, Optional<SmpProfile>> profileStore) {
        this.smpService = smpService;
        this.profileStore = profileStore;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            updateHistory(request, ((User) principal).getUsername());
        }
        authCount++;
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public void updateHistory(HttpServletRequest request, String username) {
        String ip = Utils.getIp(request);
        String userAgent = Utils.getUserAgent(request);

        profileStore.fetch(username).ifPresent(profile -> {
            HistoryModel history = new HistoryModel()
                    .setAccount(new AccountModel().setId(profile.id()))
                    .setIp(ip)
                    .setUserAgent(userAgent)
                    .setAction((short) HistoryType.AUTHORIZATION_SITE.ordinal());
            smpService.saveHistory(history);
        });
    }

    public int getAuthCount() {
        return authCount;
    }
}
