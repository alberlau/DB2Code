package org.db2code.extractors;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.md.ProcedureColumnMetadata;
import org.db2code.rawmodel.RawProcedureParameter;

public class ProcedureParametersExtractor extends AbstractExtractor<DatabaseExtractionParameters> {
    @Override
    public List<RawProcedureParameter> extract(
            DatabaseMetaData databaseMetaData, DatabaseExtractionParameters params) {
        try {
            return _extract(databaseMetaData, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<RawProcedureParameter> _extract(
            DatabaseMetaData databaseMetaData, DatabaseExtractionParameters params)
            throws SQLException {
        List<RawProcedureParameter> rawProcedureParameters = new ArrayList<>();
        try (ResultSet procedureColumnsRs =
                databaseMetaData.getProcedureColumns(
                        params.getCatalog(),
                        params.getSchemaPattern(),
                        params.getProcedureNamePattern(),
                        null)) {
            while (procedureColumnsRs.next()) {
                RawProcedureParameter procedureParameter = new RawProcedureParameter();
                for (ProcedureColumnMetadata procedureColumnMetadata :
                        ProcedureColumnMetadata.values()) {
                    String mdValue =
                            tryGetFromMetadata(procedureColumnMetadata, procedureColumnsRs);
                    String propName =
                            JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(
                                    procedureColumnMetadata.getName());
                    setProperty(procedureParameter, mdValue, propName);
                }
                rawProcedureParameters.add(procedureParameter);
            }
        }

        return rawProcedureParameters;
    }
}
