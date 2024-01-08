package org.db2code.extractors;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.md.ProcedureMetadata;
import org.db2code.rawmodel.AbstractRawProcedureItem;
import org.db2code.rawmodel.RawProcedure;
import org.db2code.rawmodel.RawProcedureParameter;

public class ProcedureExtractor extends AbstractExtractor<DatabaseExtractionParameters> {

    private final ProcedureParametersExtractor procedureParametersExtractor =
            new ProcedureParametersExtractor();

    @Override
    public List<RawProcedure> extract(
            DatabaseMetaData databaseMetaData, DatabaseExtractionParameters params) {
        try {
            return _extract(databaseMetaData, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<RawProcedure> _extract(
            DatabaseMetaData databaseMetaData, DatabaseExtractionParameters params)
            throws SQLException {

        List<RawProcedureParameter> procedureParameters =
                procedureParametersExtractor.extract(databaseMetaData, params);
        Map<String, List<RawProcedureParameter>> parametersByProcedure =
                procedureParameters.stream()
                        .collect(Collectors.groupingBy(item -> generateKey(item)));

        List<RawProcedure> rawProcedures = new ArrayList<>();
        try (ResultSet proceduresRs =
                databaseMetaData.getProcedures(
                        params.getCatalog(),
                        params.getSchemaPattern(),
                        params.getProcedureNamePattern())) {
            RawProcedure procedure = null;
            while (proceduresRs.next()) {
                procedure = new RawProcedure();
                for (ProcedureMetadata procedureMetadata : ProcedureMetadata.values()) {
                    String mdValue = tryGetFromMetadata(procedureMetadata, proceduresRs);
                    String propName =
                            JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(
                                    procedureMetadata.getName());
                    setProperty(procedure, mdValue, propName);
                }
                List<RawProcedureParameter> parameterList =
                        parametersByProcedure.get(generateKey(procedure));
                if (parameterList != null && parameterList.size() > 0) {
                    parameterList.get(parameterList.size() - 1).setIsLast(true);
                }
                procedure.setParameters(parameterList);
                rawProcedures.add(procedure);
            }
            if (procedure != null) {
                procedure.setIsLast(true);
            }
        }

        return rawProcedures;
    }

    private static String generateKey(AbstractRawProcedureItem item) {
        return StringUtils.joinWith(
                ".", item.getProcedureCat(), item.getProcedureSchem(), item.getProcedureName());
    }
}
