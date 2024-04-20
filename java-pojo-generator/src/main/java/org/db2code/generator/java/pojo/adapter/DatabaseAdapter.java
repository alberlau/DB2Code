package org.db2code.generator.java.pojo.adapter;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.db2code.generator.java.pojo.ExecutorParams;
import org.db2code.rawmodel.RawDatabaseMetadata;
import org.db2code.rawmodel.RawTable;

public class DatabaseAdapter {
    private final RawDatabaseMetadata rawDatabaseMetadata;
    private final ExecutorParams params;

    public DatabaseAdapter(RawDatabaseMetadata rawDatabaseMetadata, ExecutorParams params) {
        this.rawDatabaseMetadata = rawDatabaseMetadata;
        this.params = params;
    }

    public Collection<ClassAdapter> getClasses() {
        List<ClassAdapter> classList = new ArrayList<>();
        List<ClassAdapter> tableClassList = makeTableClassList();
        classList.addAll(tableClassList);
        List<ProcedureAdapter> procedureClassList = makeProcedureClassList();

        classList.addAll(procedureClassList);

        return classList;
    }

    private List<ProcedureAdapter> makeProcedureClassList() {
        List<ProcedureAdapter> procedureClassList =
                rawDatabaseMetadata.getProcedures().stream()
                        .map(rawProcedure -> new ProcedureAdapter(rawProcedure, params))
                        .collect(Collectors.toList());
        setIsLast(procedureClassList);
        return procedureClassList;
    }

    private static void setIsLast(List<? extends ClassAdapter> classList) {
        // select last element from classList
        if (CollectionUtils.isNotEmpty(classList)) {
            classList.get(classList.size() - 1).setLast(true);
        }
    }

    private List<ClassAdapter> makeTableClassList() {
        List<ClassAdapter> tableClassList =
                rawDatabaseMetadata.getTables().stream()
                        .filter(rawTable -> shouldGenerate(rawTable))
                        .map(
                                (RawTable rawTable) ->
                                        new DefaultClassAdapter(
                                                rawTable,
                                                params.getGeneratorTarget().getTargetPackage(),
                                                params.getDateImpl(),
                                                params.getTypeMapFile(),
                                                params.isIncludeGenerationInfo()))
                        .collect(Collectors.toList());
        setIsLast(tableClassList);
        return tableClassList;
    }

    private boolean shouldGenerate(RawTable rawTable) {
        String tableName = rawTable.getTableName();
        Collection<String> doNotGenerateTables = params.getDoNotGenerateTables();
        if (isEmpty(doNotGenerateTables)) {
            return true;
        }
        return doNotGenerateTables.stream().noneMatch(tableName::matches);
    }

    public RawDatabaseMetadata getRawDatabaseMetadata() {
        return rawDatabaseMetadata;
    }

    public ExecutorParams getParams() {
        return params;
    }
}
