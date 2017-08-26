package com.heller;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogLine {
    private String mLine;
    private static Matcher m = null;
    private static final Pattern p = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) - - \\[(.*)\\] \\\"(.*) (\\/.*) HTTP\\/\\d\\.\\d\\\" (\\d{1,}) (\\d*) \\\".*\\\" \\\"(.*)\\\" (\\d*)");
    /*
    REGEX GROUPS
    0 = whole string
    1 = IP
    2 = Date Time Group
    3 = verb
    4 = URL
    5 = Response Code
    6 = Response Time
    7 = user Agent
    8 = process id??
     */
    public LogLine(String xLine) {
        mLine = xLine;
        m = p.matcher(mLine);
        m.find();
//        System.out.printf("1: %s\n", m.group(1));
//        System.out.printf("2: %s\n", m.group(2));
//        System.out.printf("3: %s\n", m.group(3));
//        System.out.printf("4: %s\n", m.group(4));
//        System.out.printf("5: %s\n", m.group(5));
//        System.out.printf("6: %s\n", m.group(6));
//        System.out.printf("7: %s\n", m.group(7));
//        System.out.printf("8: %s\n", m.group(8));
    }
    public int getTime() {
        return Integer.parseInt(m.group(6));
    }
    public String getUrl() {
        return m.group(4);
    }
    public int getRespCode() {
        return Integer.parseInt(m.group(5));
    }
    public String getUserAgent() {
        return m.group(7);
    }
}
