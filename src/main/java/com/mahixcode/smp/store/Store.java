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

/**
 * @author maximen39
 */
public interface Store<K, V> {

    /**
     * Fetch value from store by key
     *
     * @param key key
     * @return value or null if doesn't present
     */
    V fetch(K key);

    /**
     * Store given value by given key
     *
     * @param key   key for store
     * @param value value for store
     * @return stored value
     */
    V store(K key, V value);

    /**
     * Remove value from store by key
     *
     * @param key key
     * @return removed value or null if doesn't present
     */
    V remove(K key);
}
