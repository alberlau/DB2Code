package org.db2code.md;

public enum TableMetadata implements ResultsetMetadata {
    TABLE_CAT,
    TABLE_SCHEM,
    TABLE_NAME,
    TABLE_TYPE,
    REMARKS,
    TYPE_CAT,
    TYPE_SCHEM,
    TYPE_NAME,
    SELF_REFERENCING_COL_NAME,
    REF_GENERATION;

    @Override
    public String getName() {
        return name();
    }
}
