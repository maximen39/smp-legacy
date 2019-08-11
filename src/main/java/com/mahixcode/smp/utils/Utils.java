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
package com.mahixcode.smp.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.random.MersenneTwister;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * @author maximen39
 */
public class Utils {
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Europe/Moscow");
    private static final String GOOGLE_RECAPTCHA_API_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$");
    public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",./?";
    public static final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final MersenneTwister MERSENNE_TWISTER = new MersenneTwister();

    public static boolean checkUsername(String username) {
        if (username.length() > 16 || username.length() < 3) return false;
        Matcher matcher = NAME_PATTERN.matcher(username);
        return matcher.matches();
    }

    public static boolean checkPassword(String password) {
        return password.length() <= 32 && password.length() >= 6;
    }

    public static boolean checkEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static String SHA512(String target) {
        String generatedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(target.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            generatedPassword = hashtext.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return generatedPassword;
    }

    public static String SHA256(String target) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(target.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] SHA256Digest(String target) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(target.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String MD5(String target) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(target.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] MD5Digest(String target) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return digest.digest(target.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String randomString(int min, int max, String characters) {
        return RandomStringUtils.random(mtRand(min, max), characters);
    }

    public static int mtRand(int start, int stop) {
        if (stop <= start) {
            throw new IllegalArgumentException("stop >= start");
        }
        return (MERSENNE_TWISTER.nextInt((stop - start) + 1) + start);
    }

    public static double toFixed(double number, int scale) {
        BigDecimal numberBigDecimal = new BigDecimal(number);
        return numberBigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static boolean isAuth() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    public static Optional<User> getUser() {
        if (!isAuth()) {
            return Optional.empty();
        }
        return Optional.of((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    public static void logout(HttpServletRequest request) throws ServletException {
        request.logout();
        SecurityContextHolder.clearContext();
    }

    public static String getIp(HttpServletRequest request) {
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isEmpty()) {
            return realIp;
        }
        String forwardedIp = request.getHeader("X-FORWARDED-FOR");
        if (forwardedIp != null && !forwardedIp.isEmpty()) {
            return forwardedIp;
        }
        return request.getRemoteAddr();
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    private static Function<MessageSourceResolvable, String> defaultMessageBindingResult() {
        return MessageSourceResolvable::getDefaultMessage;
    }

    private static Function<ObjectError, String> defaultArgumentBindingResult() {
        return objectError -> ((MessageSourceResolvable) requireNonNull(objectError.getArguments())[0]).getDefaultMessage();
    }

    public static Map<String, String> fromBindingResult(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream().collect(Collectors.toMap(
                defaultArgumentBindingResult(),
                defaultMessageBindingResult(),
                (a, b) -> b)
        );
    }

    /*    public static boolean recaptchaCheck(Gson gson, Config config, String token, String action, String ip) throws IOException {
     *//*       System.out.println("[recaptchaCheck]: Action: " + action);
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(GOOGLE_RECAPTCHA_API_URL);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("secret", config.recaptchaSecretKey));
        urlParameters.add(new BasicNameValuePair("response", token));
        urlParameters.add(new BasicNameValuePair("remoteip", ip));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(post);

        if (response.getStatusLine().getStatusCode() != 200) {
            return false;
        }

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String line = IOUtils.toString(rd);

        RecaptchaResponse recaptchaResponse = gson.fromJson(line, RecaptchaResponse.class);
        if (recaptchaResponse.isSuccess() && recaptchaResponse.getScore() >= 0.5 &&
                recaptchaResponse.getAction().equalsIgnoreCase(action)) {
            return true;
        } else {
            System.out.println("[recaptchaCheck]: " + ip + " не прошел капчу|" + recaptchaResponse.toString());
            return false;
        }*//*
        return true;
    }*/
}
