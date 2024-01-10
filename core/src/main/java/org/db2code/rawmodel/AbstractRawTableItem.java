package org.db2code.rawmodel;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class AbstractRawTableItem extends AbstractRawItem {
    private String tableCat;
    private String tableSchem;
    private String tableName;
}
