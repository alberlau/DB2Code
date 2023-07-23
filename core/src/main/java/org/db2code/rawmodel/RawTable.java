package org.db2code.rawmodel;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;

@Data
public class RawTable extends AbstractRawItem {
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
    public static class RawPrimaryKey extends AbstractRawItem {
        private String columnName;
        private String keySeq;
        private String pkName;
    }
}
