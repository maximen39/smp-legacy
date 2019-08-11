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
public class MainController {
/*    private static final Permission DEFAULT_PERMISSION = Permission.USER;

    private final CmsService cmsService;
    private final PasswordEncoder passwordEncoder;
    private final Store<String, Optional<CmsProfile>> profileStore;
    private final EmailFactory emailFactory;

    public MainController(CmsService cmsService, PasswordEncoder passwordEncoder, Store<String,
            Optional<CmsProfile>> profileStore, EmailFactory emailFactory) {
        this.cmsService = cmsService;
        this.passwordEncoder = passwordEncoder;
        this.profileStore = profileStore;
        this.emailFactory = emailFactory;
    }

    @GetMapping("/")
    private ModelAndView index() {
        return new ModelAndView("index");
    }

    @ResponseBody
    @PostMapping("/processing/registration")
    private Object registration(@Valid RegistrationForm form, BindingResult bindingResult, HttpServletRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        if (bindingResult.hasErrors()) {
            responseBody.put("errors", bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
            );
            return responseBody;
        }

        if (cmsService.getUserByName(form.getName()).isPresent()) {
            responseBody.put("errors", Collections.singletonList("Ник уже используется"));
            return responseBody;
        }

        if (cmsService.getUserByEmail(form.getEmail()).isPresent()) {
            responseBody.put("errors", Collections.singletonList("Почта уже используется"));
            return responseBody;
        }

        String encodedPassword = passwordEncoder.encode(form.getPassword());
        String ip = Utils.getIp(request);

        User user = new User()
                .name(form.getName())
                .email(form.getEmail())
                .permission(DEFAULT_PERMISSION)
                .password(encodedPassword);

        long id = cmsService.saveUser(user);

        CmsProfile cmsProfile = new CmsProfile(
                form.getName(),
                form.getEmail(),
                DEFAULT_PERMISSION,
                encodedPassword
        );
        cmsProfile.id(id);

        profileStore.store(
                form.getName(),
                Optional.of(cmsProfile)
        );

        History history = new History()
                .user(new User().id(id))
                .ip(ip)
                .historyType(HistoryType.REGISTRATION);
        cmsService.saveHistory(history);

        responseBody.put("success", "Вы успешно зарегистрировались :)");
        return responseBody;
    }

    @ResponseBody
    @PostMapping("/processing/recovery")
    private Object recovery(@Valid RecoveryForm form, BindingResult bindingResult, HttpServletRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        if (bindingResult.hasErrors()) {
            responseBody.put("errors", bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
            );
            return responseBody;
        }

        Optional<User> optionalUser;
        optionalUser = cmsService.getUserByName(form.getNameOrEmail());
        if (!optionalUser.isPresent()) {
            optionalUser = cmsService.getUserByEmail(form.getNameOrEmail());
        }

        responseBody.put("success", "Если вы указали правильные данные, то письмо будет отправлено вам на почту");
        if (!optionalUser.isPresent()) {
            return responseBody;
        }

        User user = optionalUser.get();
        String email = user.email();
        String newPassword = Utils.randomString(12, 16, Utils.PASSWORD_CHARACTERS);

        emailFactory.sendMessage(email, "title", "ваш ник: $username, Ваш новый пароль: $newPassword"
                .replace("$username", user.name())
                .replace("$newPassword", newPassword)
        );

        user.password(passwordEncoder.encode(newPassword));
        cmsService.saveUser(user);
        profileStore.remove(user.name());

        History history = new History()
                .user(user)
                .ip(Utils.getIp(request))
                .historyType(HistoryType.RECOVERY_PASSWORD);
        cmsService.saveHistory(history);

        return responseBody;
    }*/
}
