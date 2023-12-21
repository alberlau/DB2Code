package org.db2code.generator.java.pojo.adapter;

import java.util.Collection;
import java.util.stream.Collectors;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.rawmodel.RawTable;

public class JavaClassAdapter {
    private final RawTable rawTable;
    private final String targetPackage;
    private final DateImpl dateImpl;

    public JavaClassAdapter(RawTable rawTable, String targetPackage, DateImpl dateImpl) {
        this.rawTable = rawTable;
        this.targetPackage = targetPackage;
        this.dateImpl = dateImpl;
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
                .map(rawColumn -> new JavaPropertyAdapter(rawTable, rawColumn, dateImpl))
                .collect(Collectors.toList());
    }
}
