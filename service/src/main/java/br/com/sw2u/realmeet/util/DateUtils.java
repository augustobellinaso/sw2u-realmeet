package br.com.sw2u.realmeet.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class DateUtils {
    
    private static final ZoneOffset DEFAULT_TIME_ZONE = ZoneOffset.of("-03:00");
    
    private DateUtils() {
    }
    
    public static OffsetDateTime now() {
        return OffsetDateTime.now(DEFAULT_TIME_ZONE);
    }
    
}
