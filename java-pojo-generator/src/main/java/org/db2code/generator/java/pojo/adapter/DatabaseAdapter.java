package org.db2code.generator.java.pojo.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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
        return procedureClassList;
    }

    private List<ClassAdapter> makeTableClassList() {
        List<ClassAdapter> tableClassList =
                rawDatabaseMetadata.getTables().stream()
                        .map(
                                (RawTable rawTable) ->
                                        new DefaultClassAdapter(
                                                rawTable,
                                                params.getGeneratorTarget().getTargetPackage(),
                                                params.getDateImpl(),
                                                params.getTypeMapFile(),
                                                params.isIncludeGenerationInfo()))
                        .collect(Collectors.toList());
        return tableClassList;
    }

    public RawDatabaseMetadata getRawDatabaseMetadata() {
        return rawDatabaseMetadata;
    }
}