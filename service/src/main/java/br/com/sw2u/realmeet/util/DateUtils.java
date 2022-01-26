package br.com.sw2u.realmeet.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.MILLIS;

public final class DateUtils {
    
    public static final ZoneOffset DEFAULT_TIME_ZONE = ZoneOffset.of("-03:00");
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm";
    
    private DateUtils() {
    }
    
    public static OffsetDateTime now() {
        return OffsetDateTime.now(DEFAULT_TIME_ZONE).truncatedTo(MILLIS);
    }
    
    public static boolean isOverlapping(OffsetDateTime start1, OffsetDateTime end1, OffsetDateTime start2, OffsetDateTime end2) {
        
        return start1.compareTo(end2) < 0 && end1.compareTo(start2) > 0;
    }
    
    public static String formatUsingDatePattern(LocalDate localDate) {
        return Objects.requireNonNull(localDate).format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }
    
    public static String formatUsingDateTimePattern(OffsetDateTime localDate) {
        return Objects.requireNonNull(localDate).format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }
    
}
