package org.db2code.rawmodel;

import lombok.Data;

@Data
public class AbstractRawProcedureItem extends AbstractRawItem {
    private String procedureCat;
    private String procedureSchem;
    private String procedureName;
}
