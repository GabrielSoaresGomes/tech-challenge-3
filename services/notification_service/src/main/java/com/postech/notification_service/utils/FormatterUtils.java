package com.postech.notification_service.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatterUtils {
    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        return dateTime.format(formatter);
    }

    public static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        return dateTime.format(formatter);
    }
}
