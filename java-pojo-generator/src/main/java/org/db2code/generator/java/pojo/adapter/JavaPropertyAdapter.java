package org.db2code.generator.java.pojo.adapter;

import static org.db2code.generator.java.pojo.adapter.SqlTypeToJavaMapper.JAVA_STRING;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.rawmodel.RawColumn;
import org.db2code.rawmodel.RawTable;

@Slf4j
public class JavaPropertyAdapter {

    private final SqlTypeToJavaMapper sqlTypeToJavaMapper;
    private final RawColumn rawColumn;

    @SuppressWarnings("PMD.UnusedPrivateField")
    private final RawTable rawTable;

    private final Set<String> primaryKeyColumns;

    public JavaPropertyAdapter(RawTable rawTable, RawColumn rawColumn, DateImpl dateImpl) {
        this.rawTable = rawTable;
        this.rawColumn = rawColumn;
        sqlTypeToJavaMapper = new SqlTypeToJavaMapper(dateImpl);
        this.primaryKeyColumns =
                rawTable.getPrimaryKey().stream()
                        .map(RawTable.RawPrimaryKey::getColumnName)
                        .collect(Collectors.toUnmodifiableSet());
    }

    public RawColumn getRawColumn() {
        return rawColumn;
    }

    public String getJavaType() {
        return sqlTypeToJavaMapper.getJavaType(rawColumn.getDataType());
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

    public Boolean getIsNullable() {
        return "YES".equals(rawColumn.getIsNullable())
                ? Boolean.TRUE
                : "NO".equals(rawColumn.getIsNullable()) ? Boolean.FALSE : null;
    }

    public Integer getSize() {
        if (Objects.equals(sqlTypeToJavaMapper.getJavaType(rawColumn.getDataType()), JAVA_STRING)) {
            return rawColumn.getColumnSize();
        } else {
            return null;
        }
    }
}
