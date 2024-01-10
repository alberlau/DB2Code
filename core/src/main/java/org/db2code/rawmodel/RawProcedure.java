package org.db2code.rawmodel;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class RawProcedure extends AbstractRawProcedureItem {
    private String remarks;
    private String procedureType;
    private String specificName;
    private Collection<RawProcedureParameter> parameters = new ArrayList<>();
}
