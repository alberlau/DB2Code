package org.db2code;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import org.db2code.extractors.ColumnExtractor;
import org.db2code.extractors.ExtractionParameters;
import org.db2code.extractors.TableExtractor;
import org.db2code.rawmodel.RawColumn;
import org.db2code.rawmodel.RawDatabaseMetadata;
import org.db2code.rawmodel.RawTable;

public class MetadataExtractor {
    private final ConnectionProvider connectionProvider;

    public MetadataExtractor(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public RawDatabaseMetadata extract(ExtractionParameters extractionParameters) {
        DatabaseMetaData databaseMetaData;
        try {
            databaseMetaData = connectionProvider.getConnection().getMetaData();
            List<RawTable> tablesResults =
                    new TableExtractor().extract(databaseMetaData, extractionParameters);
            List<RawColumn> columnsResult =
                    new ColumnExtractor().extract(databaseMetaData, extractionParameters);
            tablesResults.forEach(
                    rawTable -> {
                        rawTable.setColumns(
                                columnsResult.stream()
                                        .filter(
                                                rawColumn ->
                                                        rawColumn
                                                                        .getTableName()
                                                                        .equals(
                                                                                rawTable
                                                                                        .getTableName())
                                                                && rawColumn
                                                                        .getTableSchem()
                                                                        .equals(
                                                                                rawTable
                                                                                        .getTableSchem())
                                                                && rawColumn
                                                                        .getTableCat()
                                                                        .equals(
                                                                                rawTable
                                                                                        .getTableCat()))
                                        .collect(Collectors.toList()));
                    });
            RawDatabaseMetadata rawDatabaseMetadata = new RawDatabaseMetadata();
            rawDatabaseMetadata.setTables(tablesResults);
            return rawDatabaseMetadata;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
