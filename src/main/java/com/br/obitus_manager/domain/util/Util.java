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

    public static String formatDuration(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder("Faltam ");

        if (hours > 0) {
            sb.append(hours).append(" hora").append(hours > 1 ? "s" : "");
            sb.append(" e ");
        }

        if (minutes > 0 || hours > 0) {
            sb.append(minutes).append(" minuto").append(minutes != 1 ? "s" : "");
            sb.append(" e ");
        }

        sb.append(seconds).append(" segundo").append(seconds != 1 ? "s" : "");

        return sb.toString();
    }
}
