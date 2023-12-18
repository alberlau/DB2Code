package org.db2code.rawmodel;

import lombok.Data;

@Data
public class RawColumn extends AbstractRawItem {
    private String columnName;
    private int dataType;
    private String typeName;
    private int columnSize;
    private int decimalDigits;
    private Integer numPrecRadix;
    private int nullable;
    private String remarks;
    private String columnDef;
    private String sqlDataType;
    private String sqlDatetimeSub;
    private int charOctetLength;
    private int ordinalPosition;
    private String isNullable;
}
