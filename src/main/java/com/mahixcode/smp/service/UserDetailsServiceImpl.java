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

import com.mahixcode.smp.entity.SessionUser;
import com.mahixcode.smp.entity.SmpProfile;
import com.mahixcode.smp.store.Store;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * @author maximen39
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String NOT_FOUND = "User not found exception";
    private final Store<String, Optional<SmpProfile>> profileStore;

    public UserDetailsServiceImpl(Store<String, Optional<SmpProfile>> profileStore) {
        this.profileStore = profileStore;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SmpProfile profile = profileStore.fetch(username)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND));
        return profile.sessionUser(createUser(
                profile.username(),
                profile.groups() == null || profile.groups().isEmpty() ?
                        Collections.singleton(new SimpleGrantedAuthority("default")) : profile.groups(),
                profile.password())
        ).sessionUser();
    }

    private SessionUser createUser(String username, Collection<? extends GrantedAuthority> groups, String password) {
        return new SessionUser(username, password, groups);
    }
}
