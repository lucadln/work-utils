package date;

import java.io.*;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class TmpFileParser {
    public static void main(String args[]) throws ParseException {

        final int TIME_OFFSET = -9;
        final String FILE_PATH = "/home/lucian/Activity/2022-09/md-10071-ema-delays/data-log/per-instrument-0909/JTIZ2/md10077_ema.data.20220909_JTIZ2_2_relevant";

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(FILE_PATH));
            String line = reader.readLine();

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH + "_DELAYS"));

            while (line != null) {
                System.out.println("LINE: " + line);
                String logLineTime = "2022-09-08 " + line.substring(0, 12);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                LocalDateTime date = LocalDateTime.parse(logLineTime, formatter);
                date = date.plusHours(TIME_OFFSET);

                formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                String timeWithOffset = date.format(formatter);

                int hours = Integer.parseInt(timeWithOffset.substring(0,2));
                int minutes = Integer.parseInt(timeWithOffset.substring(3,5));
                int seconds = Integer.parseInt(timeWithOffset.substring(6,8));
                int millis = Integer.parseInt(timeWithOffset.substring(9,12));
                long millisSinceMidnight = millis + (seconds * 1000L) + (minutes * 60 * 1000L) + (hours * 60 * 60 * 1000L);

                // Read value for QUOTIM_MS
                String quotimSubstring = line.substring(line.indexOf(",3855=") + 6);
                if (quotimSubstring.contains(",")) {
                    quotimSubstring = quotimSubstring.substring(0, quotimSubstring.indexOf(","));
                }

                // Calculate delay
                long delay = millisSinceMidnight - Long.parseLong(quotimSubstring);
                writer.write(logLineTime.substring(11) + "," + delay + "\n");

                // read next line
                line = reader.readLine();
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
