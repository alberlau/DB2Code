package org.db2code.extractors;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.md.ColumnMetadata;
import org.db2code.rawmodel.RawColumn;

public class ColumnExtractor extends AbstractExtractor<DatabaseExtractionParameters> {
    public List<RawColumn> extract(
            DatabaseMetaData databaseMetaData, DatabaseExtractionParameters params) {
        try {
            return _extract(databaseMetaData, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<RawColumn> _extract(
            DatabaseMetaData databaseMetaData, DatabaseExtractionParameters params)
            throws SQLException {
        List<RawColumn> results = new ArrayList<>();
        try (ResultSet columns =
                databaseMetaData.getColumns(
                        params.getCatalog(),
                        params.getSchemaPattern(),
                        params.getTableNamePattern(),
                        null)) {
            while (columns.next()) {
                RawColumn rawColumn = new RawColumn();
                for (ColumnMetadata mdItem : ColumnMetadata.values()) {
                    Object mdValue = columns.getObject(mdItem.name());
                    String propName =
                            JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(mdItem.name());
                    setProperty(rawColumn, mdValue, propName);
                }
                results.add(rawColumn);
            }
            return results;
        }
    }
}
