package bag_of_tasks;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogRunTime {

    public static String createFile() {
        try {
            String date = getCurrentDate();
            String fileName = "Logs\\"+date+".txt";
            File newLog = new File(fileName);
            if (newLog.createNewFile()) {
                System.out.println("New Log created: " + newLog.getName());
                return fileName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(String fileName, String input) {
        try {
            if (fileName != null) {
                FileWriter f = new FileWriter(fileName, true);
                PrintWriter data = new PrintWriter(new BufferedWriter(f));
                data.println(input);
                data.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
