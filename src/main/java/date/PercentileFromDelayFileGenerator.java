package date;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PercentileFromDelayFileGenerator {
    public static void main(String args[]) throws ParseException {

        String delayFile = "/home/lucian/Activity/2022-08/md-10071-ema-delays/data-logs/per-instrument-0816/2437.JNF/md10082_uat.data.20220816_2437_DELAYS";
        BufferedReader reader;
        List<Long> delays = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(delayFile));
            String line = reader.readLine();

            BufferedWriter writer = new BufferedWriter(new FileWriter(delayFile + "_SORTED"));

            while (line != null) {
                long delay = Long.parseLong(line.substring(13));
                delays.add(delay);

                // read next line
                line = reader.readLine();
            }

            Collections.sort(delays);
            for (long delay: delays) {
                writer.write(delay + "\n");
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(delayFile + "_PERCENTILES"));
            writer.write("50th percentile: " + delays.get((int) (0.5 * delays.size())) + "\n");
            writer.write("90th percentile: " + delays.get((int) (0.9 * delays.size())) + "\n");
            writer.write("95th percentile: " + delays.get((int) (0.95 * delays.size())) + "\n");
            writer.write("99th percentile: " + delays.get((int) (0.99 * delays.size())) + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
