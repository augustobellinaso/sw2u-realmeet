package br.com.sw2u.realmeet.util;

import org.apache.logging.log4j.util.Strings;

import java.util.List;

public final class StringUtils {
    
    private StringUtils() {}
    
    public static String join(List<String> list) {
        return Strings.join(list, ',');
    }
}
