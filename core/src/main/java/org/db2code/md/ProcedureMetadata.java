package org.db2code.md;

public enum ProcedureMetadata implements ResultsetMetadata {
    PROCEDURE_CAT,
    PROCEDURE_SCHEM,
    PROCEDURE_NAME,
    REMARKS,
    PROCEDURE_TYPE,
    SPECIFIC_NAME;

    @Override
    public String getName() {
        return name();
    }
}
