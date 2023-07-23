package org.db2code.generator.java.pojo.adapter;

import java.util.Collection;
import java.util.stream.Collectors;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.rawmodel.RawTable;

public class JavaClassAdapter {
    private final RawTable rawTable;
    private final String targetPackage;

    public JavaClassAdapter(RawTable rawTable, String targetPackage) {
        this.rawTable = rawTable;
        this.targetPackage = targetPackage;
    }

    public String getClassName() {
        return JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(rawTable.getTableName());
    }

    public RawTable getRawTable() {
        return rawTable;
    }

    public String getPackage() {
        return targetPackage;
    }

    public Collection<JavaPropertyAdapter> getProperties() {
        return rawTable.getColumns().stream()
                .map(rawColumn -> new JavaPropertyAdapter(rawTable, rawColumn))
                .collect(Collectors.toList());
    }
}
