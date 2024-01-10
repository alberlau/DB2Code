package org.db2code.generator.java.pojo.adapter;

import java.util.Collection;
import java.util.stream.Collectors;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.rawmodel.RawTable;

public class JavaClassAdapter implements ClassAdapter {
    private final RawTable rawTable;
    private final String targetPackage;
    private final DateImpl dateImpl;
    private final boolean includeGenerationInfo;

    public JavaClassAdapter(
            RawTable rawTable,
            String targetPackage,
            DateImpl dateImpl,
            boolean includeGenerationInfo) {
        this.rawTable = rawTable;
        this.targetPackage = targetPackage;
        this.dateImpl = dateImpl;
        this.includeGenerationInfo = includeGenerationInfo;
    }

    @Override
    public String getClassName() {
        return JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(rawTable.getTableName());
    }

    public RawTable getRawTable() {
        return rawTable;
    }

    @Override
    public String getPackage() {
        return targetPackage;
    }

    public Collection<JavaPropertyAdapter> getProperties() {
        return rawTable.getColumns().stream()
                .map(rawColumn -> new JavaPropertyAdapter(rawTable, rawColumn, dateImpl))
                .collect(Collectors.toList());
    }

    @Override
    public String getGenerationInfo() {
        if (includeGenerationInfo) {
            return ClassAdapter.super.getGenerationInfo();
        } else {
            return null;
        }
    }
}
