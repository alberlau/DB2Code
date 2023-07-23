package org.db2code.rawmodel;

import lombok.Data;

@Data
public class AbstractRawItem {
    private String tableCat;
    private String tableSchem;
    private String tableName;
}
