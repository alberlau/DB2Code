package org.db2code.generator.java.pojo.adapter;

import java.time.LocalDateTime;
import java.time.ZoneId;

public interface ClassAdapter {
    String getClassName();

    String getPackage();

    default String getGenerationInfo() {
        return "Generated by DB2Code at: " + LocalDateTime.now(ZoneId.systemDefault());
    }
}