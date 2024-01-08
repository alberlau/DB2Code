package org.db2code.md;

public enum ColumnMetadata implements ResultsetMetadata {
    TABLE_CAT,
    TABLE_SCHEM,
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    TYPE_NAME,
    COLUMN_SIZE,
    DECIMAL_DIGITS,
    NUM_PREC_RADIX,
    NULLABLE,
    REMARKS,
    COLUMN_DEF,
    SQL_DATA_TYPE,
    SQL_DATETIME_SUB,
    CHAR_OCTET_LENGTH,
    ORDINAL_POSITION,
    IS_NULLABLE;

    @Override
    public String getName() {
        return name();
    }
}
