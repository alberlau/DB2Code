package org.db2code.generator.java.pojo.adapter;

import java.util.Collection;
import java.util.stream.Collectors;
import org.db2code.rawmodel.RawDatabaseMetadata;
import org.db2code.rawmodel.RawTable;

public class JavaDatabaseAdapter {
    private final RawDatabaseMetadata rawDatabaseMetadata;
    private final String targetPackage;
    private final DateImpl dateImpl;
    private final boolean includeGenerationInfo;

    public JavaDatabaseAdapter(
            RawDatabaseMetadata rawDatabaseMetadata,
            String targetPackage,
            DateImpl dateImpl,
            boolean includeGenerationInfo) {
        this.rawDatabaseMetadata = rawDatabaseMetadata;
        this.targetPackage = targetPackage;
        this.dateImpl = dateImpl;
        this.includeGenerationInfo = includeGenerationInfo;
    }

    public Collection<JavaClassAdapter> getClasses() {
        return rawDatabaseMetadata.getTables().stream()
                .map(
                        (RawTable rawTable) ->
                                new JavaClassAdapter(
                                        rawTable, targetPackage, dateImpl, includeGenerationInfo))
                .collect(Collectors.toList());
    }
}
