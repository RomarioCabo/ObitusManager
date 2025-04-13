package com.br.obitus_manager.domain.util;

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
}
