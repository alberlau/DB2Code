package org.db2code.generator.java.pojo.adapter;

import java.util.Collection;
import java.util.stream.Collectors;
import org.db2code.rawmodel.RawDatabaseMetadata;
import org.db2code.rawmodel.RawTable;

public class JavaDatabaseAdapter {
    private final RawDatabaseMetadata rawDatabaseMetadata;
    private final String targetPackage;

    public JavaDatabaseAdapter(RawDatabaseMetadata rawDatabaseMetadata, String targetPackage) {
        this.rawDatabaseMetadata = rawDatabaseMetadata;
        this.targetPackage = targetPackage;
    }

    public Collection<JavaClassAdapter> getClasses() {
        return rawDatabaseMetadata.getTables().stream()
                .map((RawTable rawTable) -> new JavaClassAdapter(rawTable, targetPackage))
                .collect(Collectors.toList());
    }
}
