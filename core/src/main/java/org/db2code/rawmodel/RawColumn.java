package org.db2code.rawmodel;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class RawColumn extends AbstractRawTableItem {
    private String columnName;
    private Integer dataType;
    private String typeName;
    private Integer columnSize;
    private Integer decimalDigits;
    private Integer numPrecRadix;
    private Integer nullable;
    private String remarks;
    private String columnDef;
    private Integer sqlDataType;
    private Integer sqlDatetimeSub;
    private Integer charOctetLength;
    private Integer ordinalPosition;
    private String isNullable;
}
