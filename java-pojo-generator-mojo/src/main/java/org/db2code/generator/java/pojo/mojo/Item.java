package org.db2code.generator.java.pojo.mojo;

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
}
