package org.db2code.generator.java.pojo.adapter;

import java.util.Set;
import java.util.stream.Collectors;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.rawmodel.RawColumn;
import org.db2code.rawmodel.RawTable;

public class JavaPropertyAdapter {
    private final RawColumn rawColumn;
    private final String JAVA_INTEGER = "Integer";
    private final String JAVA_LONG = "Long";
    private final String JAVA_BIG_DECIMAL = "java.math.BigDecimal";
    private final String JAVA_CHAR = "Char";
    private final String JAVA_STRING = "String";
    private final String JAVA_DATE = "java.util.Date";
    private final String JAVA_BYTE_ARRAY = "byte[]";
    private final String JAVA_CHAR_ARRAY = "char[]";
    private final String JAVA_OBJECT = "Object";
    private final String JAVA_BOOLEAN = "Boolean";
    private final RawTable rawTable;
    private final Set<String> primaryKeyColumns;

    public JavaPropertyAdapter(RawTable rawTable, RawColumn rawColumn) {
        this.rawTable = rawTable;
        this.rawColumn = rawColumn;
        this.primaryKeyColumns =
                rawTable.getPrimaryKey().stream()
                        .map(RawTable.RawPrimaryKey::getColumnName)
                        .collect(Collectors.toUnmodifiableSet());
    }

    public RawColumn getRawColumn() {
        return rawColumn;
    }

    public String getPropertyName() {
        return JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(rawColumn.getColumnName());
    }

    public String getMethodName() {
        return JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(rawColumn.getColumnName());
    }

    public boolean getIsId() {
        return primaryKeyColumns.contains(rawColumn.getColumnName());
    }

    public String getJavaType() {
        switch (rawColumn.getDataType()) {
            case -6:
            case 5:
            case 4:
                return JAVA_INTEGER;
            case -5:
                return JAVA_LONG;
            case 6:
            case 7:
            case 8:
            case 2:
            case 3:
                return JAVA_BIG_DECIMAL;
            case 1:
            case -15:
                return JAVA_CHAR;
            case 12:
            case -1:
            case -8:
            case -9:
            case -16:
                return JAVA_STRING;
            case 91:
            case 92:
            case 93:
            case 2013:
            case 2014:
                return JAVA_DATE;
            case -2:
            case -3:
            case -4:
            case 2004:
                return JAVA_BYTE_ARRAY;
            case 0:
            case 1111:
            case 2000:
            case 2002:
            case 2003:
                return JAVA_OBJECT;
            case 2005:
            case 2011:
                return JAVA_CHAR_ARRAY;
            case 16:
                return JAVA_BOOLEAN;
            default:
                throw new RuntimeException(
                        "Unhandled SQL type: "
                                + rawColumn.getDataType()
                                + ", name "
                                + rawColumn.getTypeName()
                                + " for column: "
                                + rawColumn.getColumnName()
                                + " in table: "
                                + rawColumn.getTableName());
        }
    }
}
