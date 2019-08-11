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
package com.mahixcode.smp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author maximen39
 */
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "goods")
@Entity
public class GoodsModel implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @JoinColumn(name = "currency_id", nullable = false)
    @ManyToOne
    private CurrencyModel currency;

    @JoinColumn(name = "promocode_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PromocodeModel promocode;

    @Column(name = "price", nullable = false, scale = 2, columnDefinition = "decimal(10,2)")
    private double price;

    @Column(name = "new_price", scale = 2, columnDefinition = "decimal(10,2)")
    private double newPrice;

    @Column(name = "type", nullable = false, columnDefinition = "int(2)")
    private short type;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Calendar updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Calendar createdAt;
}
