package br.com.sw2u.realmeet.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static java.time.temporal.ChronoUnit.MILLIS;

public final class DateUtils {
    
    public static final ZoneOffset DEFAULT_TIME_ZONE = ZoneOffset.of("-03:00");
    
    private DateUtils() {
    }
    
    public static OffsetDateTime now() {
        return OffsetDateTime.now(DEFAULT_TIME_ZONE).truncatedTo(MILLIS);
    }
    
    public static boolean isOverlapping(OffsetDateTime start1, OffsetDateTime end1, OffsetDateTime start2, OffsetDateTime end2) {
        
        return start1.compareTo(end2) < 0 && end1.compareTo(start2) > 0;
    }
    
}
