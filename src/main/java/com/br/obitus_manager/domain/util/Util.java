package com.br.obitus_manager.domain.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    public static String maskEmail(String email) {
        if (email == null || email.isBlank()) {
            return "";
        }

        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];

        if (username.length() <= 1) {
            return email;
        }

        return username.charAt(0) + "***" + "@" + domain;
    }

    public static String formatterDate(LocalDateTime localDateTime, String patter) {
        if (localDateTime == null) return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patter);
        return localDateTime.format(formatter);
    }
}
