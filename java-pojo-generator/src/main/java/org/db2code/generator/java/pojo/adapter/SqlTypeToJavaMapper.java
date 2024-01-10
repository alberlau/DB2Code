package org.db2code.generator.java.pojo.adapter;

import java.sql.Types;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlTypeToJavaMapper {
    private final DateImpl dateImpl;
    static final String JAVA_INTEGER = "Integer";
    static final String JAVA_LONG = "Long";
    static final String JAVA_BIG_DECIMAL = "java.math.BigDecimal";
    static final String JAVA_CHAR = "Character";
    static final String JAVA_STRING = "String";
    static final String JAVA_DATE = "java.util.Date";
    static final String JAVA_LOCAL_DATE = "java.time.LocalDate";
    static final String JAVA_LOCAL_DATE_TIME = "java.time.LocalDateTime";
    static final String JAVA_BYTE_ARRAY = "byte[]";
    static final String JAVA_CHAR_ARRAY = "char[]";
    static final String JAVA_OBJECT = "Object";
    static final String JAVA_BOOLEAN = "Boolean";

    public SqlTypeToJavaMapper(DateImpl dateImpl) {
        this.dateImpl = dateImpl;
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")
    String getJavaType(int dataType) {
        switch (dataType) {
            case Types.BIT:
            case Types.BOOLEAN:
                return JAVA_BOOLEAN;
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
                return JAVA_INTEGER;
            case Types.BIGINT:
                return JAVA_LONG;
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return JAVA_BIG_DECIMAL;
            case Types.CHAR:
            case Types.NCHAR:
                return JAVA_CHAR;
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.ROWID:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                return JAVA_STRING;
            case Types.DATE:
                return resolveDateImpl();
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.TIME_WITH_TIMEZONE:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return resolveTimeImpl();
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
                return JAVA_BYTE_ARRAY;
            case Types.NULL:
            case Types.OTHER:
            case Types.JAVA_OBJECT:
            case Types.STRUCT:
            case Types.ARRAY:
            case Types.DISTINCT:
            case Types.REF:
            case Types.DATALINK:
            case Types.SQLXML:
            case Types.REF_CURSOR:
            case -150:
                return JAVA_OBJECT;
            case Types.CLOB:
            case Types.NCLOB:
                return JAVA_CHAR_ARRAY;
            default:
                log.warn("Unhandled SQL type: " + dataType + " defaulting to object.");
                return JAVA_OBJECT;
        }
    }

    private String resolveDateImpl() {
        if (dateImpl == DateImpl.UTIL_DATE) {
            return JAVA_DATE;
        } else {
            return JAVA_LOCAL_DATE;
        }
    }

    private String resolveTimeImpl() {
        if (dateImpl == DateImpl.UTIL_DATE) {
            return JAVA_DATE;
        } else {
            return JAVA_LOCAL_DATE_TIME;
        }
    }
}
