package org.db2code.generator.java.core;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Item {
    private String catalog;
    private String schemaPattern;
    private String tableNamePattern;
    private String[] types;
    private String importFile;
    private String exportFile;
    private String procedureNamePattern;
    private boolean includeStoredProcedures;
}
