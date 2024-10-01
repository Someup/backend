package project.backend.business.post.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeManager {

    public static String convertToStringPattern(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentDateTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
}
