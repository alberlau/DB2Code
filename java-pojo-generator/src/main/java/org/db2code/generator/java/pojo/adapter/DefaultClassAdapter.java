package org.db2code.generator.java.pojo.adapter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.rawmodel.RawTable;

public class DefaultClassAdapter implements ClassAdapter {
    private final RawTable rawTable;
    private final String targetPackage;
    private final boolean includeGenerationInfo;

    private final Collection<PropertyAdapter> properties;
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
    private final String prefix;
    private final String suffix;

    public DefaultClassAdapter(
            RawTable rawTable,
            String targetPackage,
            DateImpl dateImpl,
            String typeMapFile,
            boolean includeGenerationInfo,
            String prefix,
            String suffix) {
        this.rawTable = rawTable;
        this.targetPackage = targetPackage;
        this.includeGenerationInfo = includeGenerationInfo;
        this.prefix = prefix;
        this.suffix = suffix;
        properties = initProperties(rawTable, dateImpl, typeMapFile);
    }

    private Collection<PropertyAdapter> initProperties(
            RawTable rawTable, DateImpl dateImpl, String typeMapFile) {
        final Collection<PropertyAdapter> properties;
        properties =
                rawTable.getColumns().stream()
                        .map(
                                rawColumn ->
                                        new PropertyAdapter(
                                                rawTable,
                                                rawColumn,
                                                dateImpl,
                                                propertyNamesUniquenessChecker,
                                                typeMapFile))
                        .collect(Collectors.toList());
        return properties;
    }

    @Override
    public String getClassName() {
        String className =
                JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(rawTable.getTableName());
        if (StringUtils.isNotBlank(prefix)) {
            className = prefix + className;
        }
        if (StringUtils.isNotBlank(suffix)) {
            className = className + suffix;
        }
        return className;
    }

    public RawTable getRawTable() {
        return rawTable;
    }

    @Override
    public String getPackage() {
        return targetPackage;
    }

    public Boolean getIsView() {
        return rawTable.getTableType().equalsIgnoreCase("VIEW");
    }

    public Boolean getIsTable() {
        return rawTable.getTableType().equalsIgnoreCase("TABLE");
    }

    public Collection<PropertyAdapter> getProperties() {
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

    @Override
    public void setLast(boolean last) {
        this.rawTable.setIsLast(last);
    }
}
