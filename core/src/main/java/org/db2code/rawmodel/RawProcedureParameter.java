package org.db2code.rawmodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("PMD.TooManyFields")
public class RawProcedureParameter extends AbstractRawProcedureItem {
    private String columnName;
    private String columnType;
    private String dataType;
    private String typeName;
    private String precision;
    private String length;
    private String scale;
    private String radix;
    private String nullable;
    private String remarks;
    private String columnDef;
    private String charOctetLength;
    private String ordinalPosition;
    private String isNullable;
    private String specificName;
    private String sqlDataType;
    private String sqlDatetimeSub;
}
