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
package com.mahixcode.smp.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;

/**
 * @author maximen39
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListResult<O extends List> extends Result<O> {

    private int itemsPerPage;
    private int currentPage;
    private int pages;
    private int count;

    public ListResult(long timestamp, O result, String message, int itemsPerPage, int currentPage,
                      int pages, int count) {
        super(timestamp, result, message);
        this.itemsPerPage = itemsPerPage;
        this.currentPage = currentPage;
        this.pages = pages;
        this.count = count;
    }

    public static <L extends List<O>, O> ListResult<L> fromList(List<O> list, int limit, int page) {
        Objects.requireNonNull(list, "list can't be null!");
        if (limit > 100 || 0 >= limit) {
            throw new IllegalStateException("limit must be between 1 and 100!");
        }
        if (limit > list.size()) {
            limit = list.size();
            //throw new IllegalStateException("limit must be equal to or less than list!");
        }
        if (0 >= page) {
            throw new IllegalStateException("page must be 1 or more!");
        }
        int pages = (int) Math.ceil((double) list.size() / (double) limit);
        if (page > pages) {
            page = pages;
        }
        page--;
        int fromIndex = page * limit;
        int toIndex = fromIndex + limit;
        if (toIndex >= list.size()) {
            toIndex = list.size();
        }

        long timestamp = System.currentTimeMillis() / 1000;
        List<O> subList = list.subList(fromIndex, toIndex);

        @SuppressWarnings("unchecked")
        ListResult<L> result = (ListResult<L>) new ListResult<>(
                timestamp, subList, null,
                limit, page + 1,
                pages, list.size()
        );
        return result;
    }
}
