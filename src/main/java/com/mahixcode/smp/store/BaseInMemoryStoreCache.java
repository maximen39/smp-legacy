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
package com.mahixcode.smp.store;

import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author maximen39
 */
public class BaseInMemoryStoreCache<K, V> implements Store<K, V> {

    private final Map<K, V> cache;
    private final Store<K, V> store;

    public BaseInMemoryStoreCache(Map<K, V> cache, Store<K, V> store) {
        this.cache = requireNonNull(cache, "cache must be not null");
        this.store = requireNonNull(store, "store must be not null");
    }

    /**
     * Fetch value if present in cache, else try fetch value from given store
     *
     * @param key key
     * @return value
     */
    @Override
    public V fetch(K key) {
        V value = cache.get(key);
        if (value == null) {
            value = store.fetch(key);
            cache.put(key, value);
        }
        return value;
    }

    /**
     * Just push in cache
     *
     * @param key   key for store
     * @param value value for store
     * @return value
     */
    @Override
    public V store(K key, V value) {
        cache.put(key, value);
        return value;
    }

    /**
     * Remove value if present in cache
     *
     * @param key key
     * @return value
     */
    @Override
    public V remove(K key) {
        V value = cache.get(key);
        if (value != null) {
            cache.remove(key);
        }
        return value;
    }

    /**
     * Flush data to original store and clear cache
     *
     * @return current store
     */
    public Store<K, V> flush() {
        cache.forEach(store::store);
        cache.clear();
        return this;
    }

    /**
     * Flush data to original store and clear cache by key
     *
     * @param key key
     * @return current store
     */
    public Store<K, V> flush(K key) {
        if (cache.containsKey(key)) {
            store.store(key, cache.remove(key));
        }
        return this;
    }
}
