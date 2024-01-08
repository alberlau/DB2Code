package org.db2code.md;

public enum PrimaryKeyMetadata implements ResultsetMetadata {
    TABLE_CAT,
    TABLE_SCHEM,
    TABLE_NAME,
    COLUMN_NAME,
    KEY_SEQ,
    PK_NAME;

    @Override
    public String getName() {
        return name();
    }
}
