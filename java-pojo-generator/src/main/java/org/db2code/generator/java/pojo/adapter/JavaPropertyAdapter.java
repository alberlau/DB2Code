package org.db2code.generator.java.pojo.adapter;

import static org.db2code.generator.java.pojo.adapter.SqlTypeToJavaMapper.JAVA_STRING;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
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
    private String propertyName;
    private String methodName;

    public JavaPropertyAdapter(
            RawTable rawTable,
            RawColumn rawColumn,
            DateImpl dateImpl,
            Function<String, Boolean> propertyNameApprover) {
        this.rawTable = rawTable;
        this.rawColumn = rawColumn;
        this.sqlTypeToJavaMapper = new SqlTypeToJavaMapper(dateImpl);
        this.primaryKeyColumns =
                rawTable.getPrimaryKey().stream()
                        .map(RawTable.RawPrimaryKey::getColumnName)
                        .collect(Collectors.toUnmodifiableSet());

        ensureValidPropertyNameAndGetter(rawColumn, propertyNameApprover);
    }

    private void ensureValidPropertyNameAndGetter(
            RawColumn rawColumn, Function<String, Boolean> propertyNameApprover) {
        String propertyName =
                JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(rawColumn.getColumnName());
        String propertyNameToTest = propertyName;
        int idx = 0;
        while (!propertyNameApprover.apply(propertyNameToTest)) {
            idx++;
            propertyNameToTest = propertyName + idx;
        }
        this.propertyName = propertyNameToTest;
        String methodName =
                JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(rawColumn.getColumnName());
        if (propertyName.equals(propertyNameToTest)) {
            this.methodName = methodName;
        } else {
            this.methodName = methodName + idx;
        }
    }

    public RawColumn getRawColumn() {
        return rawColumn;
    }

    public String getJavaType() {
        return sqlTypeToJavaMapper.getJavaType(rawColumn.getDataType());
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getMethodName() {
        return methodName;
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
