package org.db2code.generator.java.pojo.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    public Collection<ClassAdapter> getClasses() {
        List<ClassAdapter> classList = new ArrayList<>();
        List<ClassAdapter> tableClassList = makeTableClassList();
        classList.addAll(tableClassList);
        List<JavaProcedureAdapter> procedureClassList = makeProcedureClassList();

        classList.addAll(procedureClassList);

        return classList;
    }

    private List<JavaProcedureAdapter> makeProcedureClassList() {
        List<JavaProcedureAdapter> procedureClassList =
                rawDatabaseMetadata.getProcedures().stream()
                        .map(
                                rawProcedure ->
                                        new JavaProcedureAdapter(
                                                rawProcedure,
                                                targetPackage,
                                                dateImpl,
                                                includeGenerationInfo))
                        .collect(Collectors.toList());
        return procedureClassList;
    }

    private List<ClassAdapter> makeTableClassList() {
        List<ClassAdapter> tableClassList =
                rawDatabaseMetadata.getTables().stream()
                        .map(
                                (RawTable rawTable) ->
                                        new JavaClassAdapter(
                                                rawTable,
                                                targetPackage,
                                                dateImpl,
                                                includeGenerationInfo))
                        .collect(Collectors.toList());
        return tableClassList;
    }
}
