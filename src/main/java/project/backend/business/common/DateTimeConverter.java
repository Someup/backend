package project.backend.business.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {
    /**
     * pattern: yyyy.MM.dd
     */
    public static String toStringPattern1(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    /**
     * pattern: yyyy년 MM월 dd일
     */
    public static String toStringPattern2(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }

    /**
     * pattern: yy.MM.dd
     */
    public static String toStringPattern3(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
    }
}
