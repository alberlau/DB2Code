package org.db2code;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.db2code.extractors.ColumnExtractor;
import org.db2code.extractors.DatabaseExtractionParameters;
import org.db2code.extractors.TableExtractor;
import org.db2code.rawmodel.RawColumn;
import org.db2code.rawmodel.RawDatabaseMetadata;
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
            List<RawTable> tablesResults =
                    new TableExtractor().extract(databaseMetaData, extractionParameters);
            List<RawColumn> columnsResult =
                    new ColumnExtractor().extract(databaseMetaData, extractionParameters);
            tablesResults.forEach(
                    rawTable ->
                            rawTable.setColumns(filterAndProcessColumns(rawTable, columnsResult)));
            RawDatabaseMetadata rawDatabaseMetadata = new RawDatabaseMetadata();
            rawDatabaseMetadata.setTables(tablesResults);
            return rawDatabaseMetadata;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<RawColumn> filterAndProcessColumns(
            RawTable rawTable, List<RawColumn> columnsResult) {
        List<RawColumn> filteredColumns =
                columnsResult.stream()
                        .filter(isApplicableFor(rawTable))
                        .collect(Collectors.toList());
        if (!filteredColumns.isEmpty()) {
            filteredColumns.get(filteredColumns.size() - 1).setIsLast(true);
        }
        return filteredColumns;
    }

    private static Predicate<RawColumn> isApplicableFor(RawTable rawTable) {
        return rawColumn ->
                rawColumn.getTableName().equals(rawTable.getTableName())
                        && rawColumn.getTableSchem().equals(rawTable.getTableSchem())
                        && rawColumn.getTableCat().equals(rawTable.getTableCat());
    }
}
