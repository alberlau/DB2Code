package org.db2code.rawmodel;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class RawTable extends AbstractRawTableItem {
    private String tableType;
    private String remarks;
    private String typeCat;
    private String typeSchem;
    private String typeName;
    private String selfReferencingColName;
    private String refGeneration;
    private Collection<RawColumn> columns = new ArrayList<>();

    private Collection<RawPrimaryKey> primaryKey = new ArrayList<>();
    private Collection<RawForeignKey> foreignKeys;

    @Data
    @ToString(callSuper = true)
    public static class RawPrimaryKey extends AbstractRawTableItem {
        private String columnName;
        private String keySeq;
        private String pkName;
    }
}
