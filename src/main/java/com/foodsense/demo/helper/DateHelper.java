package com.foodsense.demo.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    static LocalDateTime now = LocalDateTime.now();

    public static String getPresentTime(){
        return now.format(format);
    }
}
