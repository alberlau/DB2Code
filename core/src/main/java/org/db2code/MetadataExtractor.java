package org.db2code;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import org.db2code.extractors.DatabaseExtractionParameters;
import org.db2code.extractors.ProcedureExtractor;
import org.db2code.extractors.TableExtractor;
import org.db2code.rawmodel.RawDatabaseMetadata;
import org.db2code.rawmodel.RawProcedure;
import org.db2code.rawmodel.RawTable;

public class MetadataExtractor {
    private final ConnectionProvider connectionProvider;

    public MetadataExtractor(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public RawDatabaseMetadata extract(DatabaseExtractionParameters extractionParameters) {
        DatabaseMetaData databaseMetaData;
        try {
            databaseMetaData = connectionProvider.getConnection().getMetaData();
            RawDatabaseMetadata rawDatabaseMetadata = new RawDatabaseMetadata();
            rawDatabaseMetadata.setDatabaseProductName(databaseMetaData.getDatabaseProductName());
            rawDatabaseMetadata.setDatabaseProductVersion(
                    databaseMetaData.getDatabaseProductVersion());
            extractTables(extractionParameters, databaseMetaData, rawDatabaseMetadata);
            if (extractionParameters.isIncludeStoredProcedures()) {
                extractProcedures(extractionParameters, databaseMetaData, rawDatabaseMetadata);
            }
            return rawDatabaseMetadata;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void extractProcedures(
            DatabaseExtractionParameters extractionParameters,
            DatabaseMetaData databaseMetaData,
            RawDatabaseMetadata rawDatabaseMetadata) {
        ProcedureExtractor procedureExtractor = new ProcedureExtractor();
        List<RawProcedure> procedures =
                procedureExtractor.extract(databaseMetaData, extractionParameters);
        rawDatabaseMetadata.setProcedures(procedures);
    }

    private static void extractTables(
            DatabaseExtractionParameters extractionParameters,
            DatabaseMetaData databaseMetaData,
            RawDatabaseMetadata rawDatabaseMetadata) {
        List<RawTable> tablesResults =
                new TableExtractor().extract(databaseMetaData, extractionParameters);
        rawDatabaseMetadata.setTables(tablesResults);
    }
}
