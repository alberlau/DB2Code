package org.db2code.rawmodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbstractRawProcedureItem extends AbstractRawItem {
    private String procedureCat;
    private String procedureSchem;
    private String procedureName;
}
