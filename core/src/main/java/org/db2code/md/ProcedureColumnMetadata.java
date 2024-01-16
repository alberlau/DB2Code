package org.db2code.md;

public enum ProcedureColumnMetadata implements ResultsetMetadata {
    PROCEDURE_CAT,
    PROCEDURE_SCHEM,
    PROCEDURE_NAME,
    COLUMN_NAME,
    COLUMN_TYPE,
    DATA_TYPE,
    TYPE_NAME,
    PRECISION,
    LENGTH,
    SCALE,
    RADIX,
    NULLABLE,
    REMARKS,
    COLUMN_DEF,
    SQL_DATA_TYPE,
    SQL_DATETIME_SUB,
    CHAR_OCTET_LENGTH,
    ORDINAL_POSITION,
    IS_NULLABLE,
    SPECIFIC_NAME;

    @Override
    public String getName() {
        return name();
    }
}
