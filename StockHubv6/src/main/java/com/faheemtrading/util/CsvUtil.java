// src/main/java/com/faheemtrading/util/CsvUtil.java
package com.faheemtrading.util;

import java.io.FileWriter;
import java.nio.file.Path;
import java.util.List;

public final class CsvUtil {
    private CsvUtil(){}

    public static void write(Path path, List<String[]> rows) throws Exception {
        try(FileWriter fw=new FileWriter(path.toFile())){
            for(String[] r:rows){
                fw.append(String.join(",",r)).append("\n");
            }
        }
    }
}
