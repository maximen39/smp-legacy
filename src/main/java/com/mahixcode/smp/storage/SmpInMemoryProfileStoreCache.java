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
import com.mahixcode.smp.store.BaseInMemoryStoreCache;
import com.mahixcode.smp.store.Store;

import java.util.Map;
import java.util.Optional;

/**
 * @author maximen39
 */
public class SmpInMemoryProfileStoreCache extends BaseInMemoryStoreCache<String, Optional<SmpProfile>> {

    public SmpInMemoryProfileStoreCache(Map<String, Optional<SmpProfile>> cache,
                                            Store<String, Optional<SmpProfile>> store) {
        super(cache, store);
    }
}
