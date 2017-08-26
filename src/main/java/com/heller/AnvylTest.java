package com.heller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnvylTest {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("usage");
            System.out.println("AnvylTest.jar date [log path]");
            System.out.println("where Date is formated as YYYY-MM-DD");
            System.out.println("log path is the absolute directory path to the location of logs");
            return;
        }
        AnvylTest t = new AnvylTest();
        if (t.validateDate(args[0])) {
            AnvylLogs l;
            if (!args[1].isEmpty())
                l = new AnvylLogs(args[0], args[1]);
            else
                l = new AnvylLogs(args[0], "./logs");
            l.start();
            return;
        } else {
            System.out.println("Check your date input format");
            return;
        }
    }

    private boolean validateDate(String xArg) {
        Pattern p = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
        Matcher m = p.matcher(xArg);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
