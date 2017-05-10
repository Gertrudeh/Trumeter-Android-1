

package com.iconasystems.android.trumeter.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateUtil {

    private static final DateTimeFormatter parser = ISODateTimeFormat.dateTime();

    public static long parseDate(String input) {
        return parser.parseDateTime(input).getMillis();
    }
}
