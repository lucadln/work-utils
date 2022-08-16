package date;

import java.io.*;
import java.text.ParseException;

public class TmpFileParser {
    public static void main(String args[]) throws ParseException {

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("/home/lucian/Activity/2022-08/md-10071-ema-delays/data-logs/per-instrument-0816/BHP.AX/md10082_uat.data.20220816_bhp"));
            String line = reader.readLine();

            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/lucian/Activity/2022-08/md-10071-ema-delays/data-logs/per-instrument-0816/BHP.AX/md10082_uat.data.20220816_bhp_DELAYS"));

            while (line != null) {
                System.out.println("LINE: " + line);

                // Calculate millis from midnight
                String lineTime = line.substring(0, 12);
                int hours = Integer.parseInt(lineTime.substring(0,2));
                int minutes = Integer.parseInt(lineTime.substring(3,5));
                int seconds = Integer.parseInt(lineTime.substring(6,8));
                int millis = Integer.parseInt(lineTime.substring(9,12));
                long millisSinceMidnight = millis + (seconds * 1000L) + (minutes * 60 * 1000L) + (hours * 60 * 60 * 1000L) - (9 * 60 * 60 * 1000L);

                // Read value for QUOTIM_MS
                String quotimSubstring = line.substring(line.indexOf(",3855=") + 6);
                if (quotimSubstring.contains(",")) {
                    quotimSubstring = quotimSubstring.substring(0, quotimSubstring.indexOf(","));
                }
                System.out.println("3855=" + quotimSubstring);

                // Calculate delay
                long delay = millisSinceMidnight - Long.parseLong(quotimSubstring);
                writer.write(lineTime + "," + delay + "\n");

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
