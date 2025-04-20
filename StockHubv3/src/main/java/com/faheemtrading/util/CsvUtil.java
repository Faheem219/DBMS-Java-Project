package com.faheemtrading.util;

import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

public final class CsvUtil {
    private CsvUtil(){}
    public static void write(String file, List<String[]> rows) throws Exception {
        try (Writer w = new FileWriter(file)){
            for(String[] r: rows){
                w.write(String.join(",", r));
                w.write("\n");
            }
        }
    }
}
