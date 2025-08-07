package org.db2code.generator.java.pojo.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.rawmodel.RawForeignKey;
import org.db2code.rawmodel.RawTable;

public class DefaultClassAdapter implements ClassAdapter {
    private final RawTable rawTable;
    private final String targetPackage;
    private final boolean includeGenerationInfo;

    private final Collection<PropertyAdapter> properties;
    private final Collection<RelationAdapter> oneToManyRelations;
    private final Collection<RelationAdapter> manyToOneRelations;
    private final Collection<ManyToManyRelationAdapter> manyToManyRelations;
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
            String suffix,
            Collection<RawTable> allTables) {
        this.rawTable = rawTable;
        this.targetPackage = targetPackage;
        this.includeGenerationInfo = includeGenerationInfo;
        this.prefix = prefix;
        this.suffix = suffix;
        properties = initProperties(rawTable, dateImpl, typeMapFile);
        oneToManyRelations = initOneToManyRelations();
        manyToOneRelations = initManyToOneRelations();
        manyToManyRelations = initManyToManyRelations(allTables);
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

    public Collection<RelationAdapter> getOneToManyRelations() {
        return oneToManyRelations;
    }

    public Collection<RelationAdapter> getManyToOneRelations() {
        return manyToOneRelations;
    }

    public Collection<ManyToManyRelationAdapter> getManyToManyRelations() {
        return manyToManyRelations;
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

    private Collection<RelationAdapter> initOneToManyRelations() {
        List<RelationAdapter> results = new ArrayList<>();
        rawTable.getForeignKeys()
                .forEach(
                        fk -> {
                            String className =
                                    JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(
                                            fk.getFktableName());
                            String fieldName =
                                    JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(
                                                    fk.getFktableName())
                                            + "List";
                            String methodName =
                                    JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(
                                                    fk.getFktableName())
                                            + "List";
                            results.add(
                                    new RelationAdapter(
                                            className,
                                            fieldName,
                                            methodName,
                                            fk.getFkcolumnName()));
                        });
        return results;
    }

    private Collection<RelationAdapter> initManyToOneRelations() {
        List<RelationAdapter> results = new ArrayList<>();
        rawTable.getImportedKeys()
                .forEach(
                        fk -> {
                            String className =
                                    JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(
                                            fk.getPktableName());
                            String fieldName =
                                    JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(
                                            fk.getPktableName());
                            String methodName =
                                    JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(
                                            fk.getPktableName());
                            results.add(
                                    new RelationAdapter(
                                            className,
                                            fieldName,
                                            methodName,
                                            fk.getFkcolumnName()));
                        });
        return results;
    }

    private Collection<ManyToManyRelationAdapter> initManyToManyRelations(
            Collection<RawTable> allTables) {
        List<ManyToManyRelationAdapter> results = new ArrayList<>();
        for (RawTable table : allTables) {
            Collection<RawForeignKey> imported = table.getImportedKeys();
            if (imported.size() == 2) {
                List<RawForeignKey> importedList = new ArrayList<>(imported);
                RawForeignKey fk1 = importedList.get(0);
                RawForeignKey fk2 = importedList.get(1);
                if (!fk1.getPktableName().equalsIgnoreCase(fk2.getPktableName())) {
                    if (fk1.getPktableName().equalsIgnoreCase(rawTable.getTableName())) {
                        results.add(createManyToManyRelation(fk1, fk2, table.getTableName()));
                    } else if (fk2.getPktableName().equalsIgnoreCase(rawTable.getTableName())) {
                        results.add(createManyToManyRelation(fk2, fk1, table.getTableName()));
                    }
                }
            }
        }
        return results;
    }

    private ManyToManyRelationAdapter createManyToManyRelation(
            RawForeignKey currentFk, RawForeignKey otherFk, String joinTable) {
        String className =
                JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(otherFk.getPktableName());
        String fieldName =
                JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(otherFk.getPktableName())
                        + "List";
        String methodName =
                JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(otherFk.getPktableName())
                        + "List";
        return new ManyToManyRelationAdapter(
                className,
                fieldName,
                methodName,
                currentFk.getFkcolumnName(),
                joinTable,
                otherFk.getFkcolumnName());
    }
}
