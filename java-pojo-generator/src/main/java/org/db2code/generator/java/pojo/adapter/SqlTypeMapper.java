package org.db2code.generator.java.pojo.adapter;

import java.io.IOException;
import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlTypeMapper {
    private final DateImpl dateImpl;
    private final Properties typeMap = new Properties();
    private final Map<String, String> funcMapping = new HashMap<>();

    public SqlTypeMapper(DateImpl dateImpl, String typeMapFile) {
        this.dateImpl = dateImpl;
        if (typeMapFile == null) {
            typeMapFile = "/type-mappings/java-type-map.properties";
        }
        initTypeMap(typeMapFile);
        initFuncMapping();
    }

    private void initFuncMapping() {
        funcMapping.put("resolveDateImpl()", resolveDateImpl());
        funcMapping.put("resolveTimeImpl()", resolveTimeImpl());
    }

    private void initTypeMap(String typeMapFile) {
        try {
            typeMap.load(SqlTypeMapper.class.getResourceAsStream(typeMapFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String getMappedType(int dataType) {
        String val = typeMap.getProperty(String.valueOf(dataType));
        if (val == null) {
            JDBCType jdbcType = JDBCType.valueOf(dataType);
            val = typeMap.getProperty(jdbcType.name());
            if (val == null) {
                val = typeMap.getProperty("default");
            }
            if (val == null) {
                throw new RuntimeException(
                        "Type mapping not found for jdbc dataType "
                                + dataType
                                + " and no default was provided.");
            } else {
                log.warn("Unhandled SQL type: " + dataType + " defaulting to " + val);
            }
        }

        if (val.endsWith("()")) {
            return funcMapping.get(val);
        }

        return val;
    }

    public String resolveDateImpl() {
        if (dateImpl == DateImpl.UTIL_DATE || dateImpl == null) {
            return typeMap.getProperty("JAVA_DATE");
        } else {
            return typeMap.getProperty("JAVA_LOCAL_DATE");
        }
    }

    private String resolveTimeImpl() {
        if (dateImpl == DateImpl.UTIL_DATE) {
            return typeMap.getProperty("JAVA_DATE");
        } else {
            return typeMap.getProperty("JAVA_LOCAL_DATE_TIME");
        }
    }

    public String getMappedType(String of) {
        return typeMap.getProperty(of);
    }
}
