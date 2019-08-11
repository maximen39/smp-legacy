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
package com.mahixcode.smp.controller.main;

import org.springframework.stereotype.Controller;

/**
 * @author maximen39
 */
@Controller
public class PaymentController {
/*    private static String UNITPAY_API = "https://unitpay.ru/pay/";

    @ResponseBody
    @PostMapping("/processing/payment/generate")
    public Object generatePayment(@Valid PaymentForm paymentForm, BindingResult bindingResult, HttpServletRequest request)
            throws URISyntaxException {
        if (bindingResult.hasErrors()) {
            return singletonMap("errors", fromBindingResult(bindingResult));
        }

        URIBuilder builder = new URIBuilder(UNITPAY_API + "32321-6975d");
        builder.setParameter("sum", paymentForm.getDeposit().toString());
        builder.setParameter("account", paymentForm.getDeposit().toString());
        builder.setParameter("desc", "Покупка внутриигровой валюты");
        builder.setParameter("hideBackUrl", "true");
        builder.setParameter("hideMenu", "false");

        String gateway = builder.build().toString();

        Map<String, Object> map = new HashMap<>();
        map.put("success", "Redirect to gateway...");
        map.put("response", singletonMap("gateway", gateway));

        return map;
    }

    @ResponseBody
    @PostMapping("/processing/payment/unitpay")
    public Object unitpayHandler(@Valid PaymentForm paymentForm, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return singletonMap("errors", fromBindingResult(bindingResult));
        }

        return singletonMap("success", paymentForm.getName() + " " + paymentForm.getDeposit());
    }*/
}
