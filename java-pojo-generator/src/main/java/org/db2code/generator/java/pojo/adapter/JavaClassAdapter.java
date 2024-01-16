package org.db2code.generator.java.pojo.adapter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.rawmodel.RawTable;

public class JavaClassAdapter implements ClassAdapter {
    private final RawTable rawTable;
    private final String targetPackage;
    private final boolean includeGenerationInfo;

    private final Collection<JavaPropertyAdapter> properties;
    private final Set<String> uniqueProperties = new HashSet<>();

    private final Function<String, Boolean> propertyNamesUniquenessChecker =
            (propertyName) -> {
                if (uniqueProperties.contains(propertyName)) {
                    return false;
                } else {
                    uniqueProperties.add(propertyName);
                    return true;
                }
            };

    public JavaClassAdapter(
            RawTable rawTable,
            String targetPackage,
            DateImpl dateImpl,
            boolean includeGenerationInfo) {
        this.rawTable = rawTable;
        this.targetPackage = targetPackage;
        this.includeGenerationInfo = includeGenerationInfo;

        properties = initProperties(rawTable, dateImpl);
    }

    private Collection<JavaPropertyAdapter> initProperties(RawTable rawTable, DateImpl dateImpl) {
        final Collection<JavaPropertyAdapter> properties;
        properties =
                rawTable.getColumns().stream()
                        .map(
                                rawColumn ->
                                        new JavaPropertyAdapter(
                                                rawTable,
                                                rawColumn,
                                                dateImpl,
                                                propertyNamesUniquenessChecker))
                        .collect(Collectors.toList());
        return properties;
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
        return properties;
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
