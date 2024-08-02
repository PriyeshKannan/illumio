package com.Illumio.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private int numberOfColumns = -1;
    private boolean headerPresent = false;

    public CSVReader(int numberOfColumns, boolean headerPresent) {
        this.numberOfColumns = numberOfColumns;
        this.headerPresent = headerPresent;
    }

    public String[][] load(String csvFile) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)))) {
            String str;
            List<String> list = new ArrayList<>();
            while ((str = in.readLine()) != null) {
                list.add(str);
            }
            String[] rows = list.toArray(new String[0]);
            int numRows = headerPresent ? list.size() - 1 : list.size();
            String[][] csvData = new String[numRows][this.numberOfColumns];
            int startIndex = headerPresent ? 1 : 0;
            for (int i = startIndex; i < list.size(); i++) {
                csvData[i - 1] = rows[i].split(",");
            }
            return csvData;
        }
        catch (IOException e) {
            throw e;
        }
    }
}
