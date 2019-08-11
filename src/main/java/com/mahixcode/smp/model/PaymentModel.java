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
@Table(name = "payments")
@Entity
public class PaymentModel implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "account_id", nullable = false)
    @ManyToOne
    private AccountModel account;

    @JoinColumn(name = "gateway_id", nullable = false)
    @ManyToOne
    private GatewayModel gateway;

    @Column(name = "status", nullable = false, columnDefinition = "int(1)")
    private byte status;

    @JoinColumn(name = "goods_id", nullable = false)
    @ManyToOne
    private GoodsModel goods;

    @Column(name = "deposit", scale = 2, columnDefinition = "decimal(10,2)", nullable = false)
    private double deposit;

    @Column(name = "profit", scale = 2, columnDefinition = "decimal(10,2)")
    private double profit;

    @Column(name = "pay_method", length = 30)
    private String payMethod;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Calendar updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Calendar createdAt;
}
