package org.db2code.rawmodel;

import lombok.Data;

@Data
public class RawForeignKey {
    private String pktableCat;
    private String pktableSchem;
    private String pktableName;
    private String pkcolumnName;
    private String fktableCat;
    private String fktableSchem;
    private String fktableName;
    private String fkcolumnName;
    private int keySeq;
    private Integer updateRule;
    private Integer deleteRule;
    private String fkName;
    private String pkName;
    private Integer deferrability;
    private Boolean isLast;
}
