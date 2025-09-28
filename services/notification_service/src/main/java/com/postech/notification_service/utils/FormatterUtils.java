package com.postech.notification_service.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatterUtils {
    public static String formatDateTime(LocalDateTime dateTime) {
        LocalDateTime startAt = LocalDateTime.now(); // seu startAt
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        return startAt.format(formatter);
    }

    public static String formatDate(LocalDateTime dateTime) {
        LocalDateTime startAt = LocalDateTime.now(); // seu startAt
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        return startAt.format(formatter);
    }
}
