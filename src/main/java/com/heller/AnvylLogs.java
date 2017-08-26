package com.heller;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class AnvylLogs {

    private class LogStruct {
        public int count;
        public int respTime;
        public HashMap<Integer, Integer> respCodesCount = new HashMap<>();
    }

    private String mDate = null;
    private Path mPath = null;
    private ArrayList<File> mLogFiles = new ArrayList<>();
    private File targetLog = null;

    private int mTotalValid = 0;
    private int mAverage = 0;
    private int mSlowest = 0;
    private HashMap<String, LogStruct> mLogMap = new HashMap<>();
    public AnvylLogs() {
        // empty constructor
    }

    public AnvylLogs(String xArg, String xPath) {
        mDate = xArg;
        mPath = FileSystems.getDefault().getPath(xPath);
    }
    public void start() {
        walkDirs();
        File temp_f = null;
        for (File f : mLogFiles) {
            if (f.getName().startsWith(mDate)) {
                temp_f = f;
            }
        }
        if (temp_f != null)
            targetLog = temp_f;
        else
            System.out.println("Target log file not found");
        parseLog();
        prettyPrint();

    }

    private void prettyPrint() {
        System.out.println("----- Global Averages ----");
        System.out.printf("Average Response Time: %.2f seconds\n", (float) (mAverage / mTotalValid) / 1000);
        System.out.printf("Longest Response Time: %.2f seconds\n", (float) mSlowest / 1000);
        System.out.printf("Total Requests: %s\n", mTotalValid);
        System.out.println("--------------------------");
        for(String k: mLogMap.keySet()) {
            LogStruct t = mLogMap.get(k);
            System.out.printf("Total requests to endpoint %s: %s\n", k, t.count);
            for(Integer e: t.respCodesCount.keySet()) {
                System.out.printf("Request codes to above endpoint code: %s count: %s\n", e, t.respCodesCount.get(e));
            }
            System.out.printf("avg Response time to above endpoint: %.2f seconds\n", (float) (t.respTime / t.count) / 1000);
            System.out.println("--------------------------");
        }
    }

    private void parseLog() {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(targetLog);
            br = new BufferedReader(fr);
            String line;
            mTotalValid = 0;
            mAverage = 0;
            mSlowest = 0;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null) try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseLine(String xLine) {
        LogLine l = new LogLine(xLine);
        if (l.getUserAgent().equalsIgnoreCase("Ruby"))
            return;
        if (l.getUrl().equalsIgnoreCase("/ok"))
            return;
        String tUrl = l.getUrl();
        mTotalValid += 1;
        mAverage += l.getTime();
        if (mSlowest < l.getTime())
            mSlowest = l.getTime();

        if (mLogMap.containsKey(tUrl)) {
            LogStruct tLog = mLogMap.get(tUrl);
            tLog.respTime += l.getTime();
            int respCode = l.getRespCode();
            if (tLog.respCodesCount.containsKey(respCode)) {
                int tRespCodeCount = tLog.respCodesCount.get(respCode);
                tLog.respCodesCount.put(respCode, tRespCodeCount + 1);
            } else {
                tLog.respCodesCount.put(respCode, 1);
            }
            tLog.count += 1;
            mLogMap.put(tUrl, tLog);
            // update values of url we've already seen
        } else {
            // insert values into map for new URL
            LogStruct tLog = new LogStruct();
            tLog.count = 1;
            int respCode = l.getRespCode();
            tLog.respCodesCount.put(respCode, 1);
            tLog.respTime = l.getTime();
            mLogMap.put(tUrl, tLog);
        }
    }

    private void walkDirs() {
        try {
            Files.walk(mPath)
                    .filter( path -> path.toFile().isFile())
                    .filter( path -> path.toString().endsWith(".log"))
                    .forEach( each -> mLogFiles.add(each.toFile()) );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
