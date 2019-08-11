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
package com.mahixcode.smp.controller.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mahixcode.smp.TextureType;
import com.mahixcode.smp.config.json.Config;
import com.mahixcode.smp.controller.model.AccountResult;
import com.mahixcode.smp.controller.model.ListResult;
import com.mahixcode.smp.controller.model.Result;
import com.mahixcode.smp.entity.SmpProfile;
import com.mahixcode.smp.exception.ProfileNotFoundException;
import com.mahixcode.smp.factory.EmailFactory;
import com.mahixcode.smp.model.AccountModel;
import com.mahixcode.smp.model.BanModel;
import com.mahixcode.smp.model.GroupModel;
import com.mahixcode.smp.model.HistoryModel;
import com.mahixcode.smp.service.SmpService;
import com.mahixcode.smp.service.filestorage.CaseInsensitiveFilter;
import com.mahixcode.smp.service.filestorage.CloakFileStorageService;
import com.mahixcode.smp.service.filestorage.SkinFileStorageService;
import com.mahixcode.smp.store.Store;
import com.mahixcode.smp.type.HistoryType;
import com.mahixcode.smp.utils.Utils;
import com.mahixcode.smp.validation.ChangeForm;
import com.mahixcode.smp.validation.ExchangeForm;
import com.mahixcode.smp.validation.RecoveryForm;
import com.mahixcode.smp.validation.RegistrationForm;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author maximen39
 */
@Controller
public class ApiController {
    private static final String DEFAULT_NAME = "Steve";

    private List<String> registeredUsersCache = new ArrayList<>();
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private final Config config;
    private final SmpService smpService;
    private final EmailFactory emailFactory;
    private final PasswordEncoder passwordEncoder;
    private final SkinFileStorageService skinFileStorageService;
    private final CloakFileStorageService cloakFileStorageService;
    private final Store<String, Optional<SmpProfile>> profileStore;

    public ApiController(SmpService smpService, Store<String, Optional<SmpProfile>> profileStore,
                         PasswordEncoder passwordEncoder, EmailFactory emailFactory, Config config,
                         SkinFileStorageService skinFileStorageService, CloakFileStorageService cloakFileStorageService) {
        this.smpService = smpService;
        this.profileStore = profileStore;
        this.passwordEncoder = passwordEncoder;
        this.emailFactory = emailFactory;
        this.config = config;
        this.skinFileStorageService = skinFileStorageService;
        this.cloakFileStorageService = cloakFileStorageService;
    }

    @ResponseBody
    @GetMapping("/accounts/findAccount")
    public Object findAccount(@RequestParam long id) {
        Optional<AccountModel> optionalAccount = smpService.getAccount(id);
        return optionalAccount.orElse(null);
    }

    @ResponseBody
    @GetMapping("/auth/login")
    public Result login() {
        return Result.success("Перенаправление в личный кабинет...");
    }

    @ResponseBody
    @GetMapping("/account")
    public Result account() {
        User user = Utils.getUser()
                .orElseThrow(() -> new BadCredentialsException("Вы не авторизированы!"));
        SmpProfile smpProfile = profileStore.fetch(user.getUsername())
                .orElseThrow(() -> new ProfileNotFoundException("Профиль не найден!"));

        byte[] headImage;
        byte[] skinImage;
        byte[] cloakImage;

        try {
            headImage = getHeadFromSkin(smpProfile.username(), 16);
        } catch (Exception ex) {
            headImage = new byte[0];
        }
        try {
            File imageFile = textureByName(smpProfile.username(), TextureType.SKIN);
            skinImage = imageFileToBytes(imageFile, "png");
        } catch (Exception ex) {
            skinImage = new byte[0];
        }
        try {
            File imageFile = textureByName(smpProfile.username(), TextureType.CLOAK);
            cloakImage = imageFileToBytes(imageFile, "png");
        } catch (Exception ex) {
            cloakImage = new byte[0];
        }

        AccountResult accountResult = new AccountResult(
                smpProfile.email(),
                String.valueOf(smpProfile.maxGroup().orElse(new GroupModel().setName("Игрок"))),
                smpProfile.username(),
                headImage, skinImage, cloakImage,
                Collections.emptyList(),
                smpProfile.sortedWallets(),
                Collections.emptyList()
        );

        return Result.of(accountResult);
    }

    @ResponseBody
    @GetMapping("/shop/goods")
    public Result goods() {
        return Result.of(smpService.getGoods());
    }

    @ResponseBody
    @GetMapping("/shop/buy/{id}")
    public Result goods(@PathVariable("id") long id) {
        return Result.of(smpService.getGoods());
    }

    @ResponseBody
    @PostMapping("/account/uploadSkin")
    public Result uploadSkin(@RequestParam("file") MultipartFile file) throws IOException {
        User user = Utils.getUser()
                .orElseThrow(() -> new BadCredentialsException("Вы не авторизированы!"));

        skinFileStorageService.store(file, user.getUsername() + ".png");

        return Result.success("Скин успешно установлен!");
    }

    @ResponseBody
    @PostMapping("/account/uploadCloak")
    public Result uploadCloak(@RequestParam("file") MultipartFile file) throws IOException {
        User user = Utils.getUser()
                .orElseThrow(() -> new BadCredentialsException("Вы не авторизированы!"));
        cloakFileStorageService.store(file, user.getUsername() + ".png");
        return Result.success("Плащ успешно установлен!");
    }

    @ResponseBody
    @PostMapping("/account/deleteSkin")
    public Result deleteSkin() throws IOException {
        User user = Utils.getUser()
                .orElseThrow(() -> new BadCredentialsException("Вы не авторизированы!"));
        skinFileStorageService.delete(user.getUsername() + ".png");
        return Result.success("Скин успешно удален!");
    }

    @ResponseBody
    @PostMapping("/account/deleteCloak")
    public Result deleteCloak() throws IOException {
        User user = Utils.getUser()
                .orElseThrow(() -> new BadCredentialsException("Вы не авторизированы!"));
        cloakFileStorageService.delete(user.getUsername() + ".png");
        return Result.success("Плащ успешно удален!");
    }

    @ResponseBody
    @GetMapping("/currency")
    public Result currency() {
        return Result.of(smpService.getCurrencies());
    }

    @ResponseBody
    @PostMapping("/currency/exchange")
    public Result exchange(@Valid ExchangeForm form, BindingResult bindingResult,
                           HttpServletRequest request, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ObjectError error = bindingResult.getAllErrors().get(0);
            throw new IllegalArgumentException(error.getDefaultMessage());
        }

        User user = Utils.getUser()
                .orElseThrow(() -> new BadCredentialsException("Вы не авторизированы!"));
        SmpProfile smpProfile = profileStore.fetch(user.getUsername())
                .orElseThrow(() -> new ProfileNotFoundException("Профиль не найден!"));

        smpProfile.exchange(config, form.getFrom(), form.getTo(), form.getCount());

        return Result.of("Вы успешно обменяли :)");
    }

    @ResponseBody
    @GetMapping("/auth/logout")
    public Result logout(HttpServletRequest request) throws ServletException {
        Utils.logout(request);
        return Result.success("Вы успешно вышли!");
    }

    @ResponseBody
    @PostMapping("/auth/registration")
    public Result registration(@Valid RegistrationForm form, BindingResult bindingResult,
                               HttpServletRequest request, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Result.validErrors(errorsFromBinding(bindingResult));
        }

        if (registeredUsersCache.stream().anyMatch(form.getUsername()::equalsIgnoreCase)
                || smpService.hasAccountByName(form.getUsername())) {
            registeredUsersCache.add(form.getUsername());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Result.singletonValidError("username", "Ник уже используется");
        }

        if (registeredUsersCache.stream().anyMatch(form.getEmail()::equalsIgnoreCase)
                || smpService.hasAccountByEmail(form.getEmail())) {
            registeredUsersCache.add(form.getEmail());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Result.singletonValidError("email", "Почта уже используется");
        }

        String encodedPassword = passwordEncoder.encode(form.getPassword());
        String ip = Utils.getIp(request);

        AccountModel accountModel = new AccountModel()
                .setUsername(form.getUsername())
                .setEmail(form.getEmail())
                .setPassword(encodedPassword)
                .setOnline(false);

        long id = smpService.saveAccount(accountModel);

        /*profileStore.store(
                form.getUsername(),
                SmpDatabaseProfileStore.fromAccountModel(accountModel)
        );*/

        HistoryModel history = new HistoryModel()
                .setAccount(new AccountModel().setId(id))
                .setAction((short) HistoryType.REGISTRATION.ordinal())
                .setIp(ip)
                .setUserAgent(Utils.getUserAgent(request));
        smpService.saveHistory(history);

        return Result.success("Вы успешно зарегистрировались :)");
    }

    @ResponseBody
    @PostMapping("/auth/recovery")
    private Result recovery(@Valid RecoveryForm form, BindingResult bindingResult,
                            HttpServletRequest request, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Result.validErrors(errorsFromBinding(bindingResult));
        }

        Optional<AccountModel> optionalAccountModel;
        optionalAccountModel = smpService.getAccountByName(form.getNameOrEmail());
        if (!optionalAccountModel.isPresent()) {
            optionalAccountModel = smpService.getAccountByEmail(form.getNameOrEmail());
        }

        if (!optionalAccountModel.isPresent()) {
            return Result.success("Если вы указали правильные данные, то письмо будет отправлено вам на почту");
        }

        AccountModel accountModel = optionalAccountModel.get();
        String email = accountModel.getEmail();
        String newPassword = Utils.randomString(12, 16, Utils.PASSWORD_CHARACTERS);

        emailFactory.sendMessage(email, "Восстановление пароля", "ваш ник: $username, Ваш новый пароль: $newPassword"
                .replace("$username", accountModel.getUsername())
                .replace("$newPassword", newPassword)
        );

        accountModel.setPassword(passwordEncoder.encode(newPassword));
        smpService.saveAccount(accountModel);
        profileStore.remove(accountModel.getUsername());

        HistoryModel history = new HistoryModel()
                .setAccount(accountModel)
                .setAction((short) HistoryType.RECOVERY_PASSWORD.ordinal())
                .setIp(Utils.getIp(request))
                .setUserAgent(Utils.getUserAgent(request));
        smpService.saveHistory(history);

        return Result.success("Если вы указали правильные данные, то письмо будет отправлено вам на почту");
    }

    @ResponseBody
    @PostMapping("/auth/change")
    public Result changePassword(@Valid ChangeForm form, BindingResult bindingResult,
                                 HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Result.validErrors(errorsFromBinding(bindingResult));
        }

        Optional<User> optionalUser = Utils.getUser();
        if (!optionalUser.isPresent()) {
            throw new IllegalAccessException("Spring security user not found");
        }
        Optional<SmpProfile> optionalProfile = profileStore.fetch(optionalUser.get().getUsername());

        if (!optionalProfile.isPresent()) {
            throw new IllegalAccessException("Profile not found");
        }
        SmpProfile smpProfile = optionalProfile.get();

        if (!passwordEncoder.matches(form.getOldPassword(), smpProfile.password())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Result.error("Старый пароль введен неверно!");
        }

        smpService.updatePassword(smpProfile.id(), passwordEncoder.encode(form.getPassword()));
        profileStore.remove(smpProfile.username());
        return Result.success("Вы успешно изменили пароль");
    }

    @ResponseBody
    @PostMapping("/launcher/auth")
    public Object launcher(@RequestBody ObjectNode requestBody) {
        String username = requestBody.get("username").asText();
        String password = requestBody.get("password").asText();
        String ip = requestBody.get("ip").asText();
        String hwidJson = requestBody.get("hwid").asText();
        String apiKey = requestBody.get("apiKey").asText();

        String hwid = "";
        JsonObject hwidObject = gson.fromJson(hwidJson, JsonObject.class);
        if (hwidObject != null && hwidObject.isJsonObject()) {
            String totalMemory = hwidObject.get("totalMemory").getAsString();
            String diskSerial = hwidObject.get("HWDiskSerial").getAsString();
            String processor = hwidObject.get("processorID").getAsString();
            String macAddr = hwidObject.get("macAddr").getAsString();

            if (diskSerial != null && !diskSerial.isEmpty()) {
                hwid += diskSerial.trim();
            }
            if (processor != null && !processor.isEmpty()) {
                hwid += processor.trim();
            }
            if (hwid.isEmpty() && macAddr != null && !macAddr.isEmpty()) {
                hwid += macAddr.trim();
            }
        }

        if (!apiKey.equals("OIASJdfoisaud98u")) {
            return Collections.singletonMap("error", "Oops! Api key unknown.");
        }

        Optional<SmpProfile> profileOptional = profileStore.fetch(username);
        if (!profileOptional.isPresent()) {
            return Collections.singletonMap("error", "Аккаунт не найден!");
        }

        SmpProfile smpProfile = profileOptional.get();
        if (!passwordEncoder.matches(password, smpProfile.password())) {
            return Collections.singletonMap("error", "Неверный пароль!");
        }

        HistoryModel history = new HistoryModel()
                .setAccount(new AccountModel().setId(smpProfile.id()))
                .setAction((short) HistoryType.AUTHORIZATION_LAUNCHER.ordinal())
                .setHwid(hwid.isEmpty() ? null : String.valueOf(hwid.hashCode()))
                .setIp(ip)
                .setUserAgent("MCUniverseLauncher/1.0.0.0");
        smpService.saveHistory(history);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("username", username);

        responseBody.put("permissions",
                smpProfile.hasGroup(config.launcherReservedServerGroup) ? 2 :
                        smpProfile.hasGroup(config.launcherReservedAdminGroup) ? 1 : 0);
        return responseBody;
    }

    @GetMapping("/minecraftButton")
    @ResponseBody
    public String minecraftButton(@RequestParam("server") String server) {
        return "Зайти на " + server + " [0/100]";
    }

    private List<Pair<String, String>> errorsFromBinding(BindingResult bindingResult) {
        return bindingResult
                .getAllErrors()
                .stream()
                .filter((o) -> o.getDefaultMessage() != null && o.getArguments() != null && o.getArguments().length > 0)
                .map(o -> Pair.of(
                        ((DefaultMessageSourceResolvable) o.getArguments()[0]).getDefaultMessage(),
                        o.getDefaultMessage())
                ).collect(Collectors.toList());
    }

    @GetMapping("/ban/list")
    @ResponseBody
    public ListResult bans(@RequestParam(value = "page", defaultValue = "1") int page) {
        List<BanModel> bans = smpService.getBans();
        return ListResult.fromList(bans, 20, page);
    }

    @ResponseBody
    @GetMapping(value = "/texture/findHead/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] findHead(
            @RequestParam(required = false, defaultValue = "8") int multiply,
            @PathVariable String username) throws IOException {
        return getHeadFromSkin(username, multiply);
    }

    @ResponseBody
    @GetMapping(value = "/texture/findSkin/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] findSkin(@PathVariable String username) throws IOException {
        File imageFile = textureByName(username, TextureType.SKIN);
        return imageFileToBytes(imageFile, "png");
    }

    @ResponseBody
    @GetMapping(value = "/texture/findCloak/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] findCloak(@PathVariable String username) throws IOException {
        File imageFile = textureByName(username, TextureType.CLOAK);
        return imageFileToBytes(imageFile, "png");
    }

    private byte[] getHeadFromSkin(String username, int multiply) throws IOException {
        File imageFile = textureByName(username, TextureType.HEAD);

        BufferedImage bufferedImage = ImageIO.read(imageFile);

        multiply = Math.abs(multiply);
        if (multiply < 1 || multiply > 128) {
            multiply = 1;
        }
        bufferedImage = bufferedImage.getSubimage(8, 8, 8, 8);

        BufferedImage newImage = new BufferedImage(
                bufferedImage.getWidth() * multiply,
                bufferedImage.getHeight() * multiply, bufferedImage.getType()
        );
        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                newImage.setRGB(x, y, bufferedImage.getRGB(
                        x / (newImage.getWidth() / bufferedImage.getWidth()),
                        y / (newImage.getHeight() / bufferedImage.getHeight()))
                );
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(newImage, "png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return imageInByte;
    }

    private byte[] imageFileToBytes(File imageFile, String format) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return imageInByte;
    }

    private Path textureFolder(TextureType textureType) throws IOException {
        Path path = Paths.get(String.format(config.textureFolder, textureType.dir()));
        return Files.exists(path) ? path : Files.createDirectory(path);
    }

    private File textureFile(Path textureFolder, String name) {
        File[] files = textureFolder.toFile().listFiles(new CaseInsensitiveFilter(name + ".png"));
        return files != null && files.length > 0 ? files[0] : null;
    }

    private File textureByName(String username, TextureType textureType) throws IOException {
        username = username.replace(".png", "");
        File file = textureFile(textureFolder(textureType), username);
        if ((file == null || !file.exists()) && (textureType == TextureType.SKIN || textureType == TextureType.HEAD)) {
            file = new File(textureFolder(textureType).toFile(), DEFAULT_NAME + ".png");
            if (!file.exists()) {
                saveFileFromResources("assets/texture/Steve.png", file);
            }
        }
        if (file != null && file.exists()) {
            return file;
        }
        throw new FileNotFoundException("Texture not found for " + username);
    }

    private void saveFileFromResources(String name, File saveTo) throws IOException {
        InputStream inputStream = new ClassPathResource(name).getInputStream();
        if (inputStream == null) {
            throw new FileNotFoundException("File not found " + name);
        }

        FileOutputStream outputStream = new FileOutputStream(saveTo);
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }
}
