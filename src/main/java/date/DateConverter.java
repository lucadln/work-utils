package date;

import java.util.Date;

public class DateConverter {
    public static Date convertMillisToDate(long millis) {
        return new Date(millis);
    }
}
