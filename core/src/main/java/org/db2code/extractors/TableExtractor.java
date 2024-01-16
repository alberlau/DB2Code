package org.db2code.extractors;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.md.ExportedKeyMetadata;
import org.db2code.md.PrimaryKeyMetadata;
import org.db2code.md.TableMetadata;
import org.db2code.rawmodel.RawColumn;
import org.db2code.rawmodel.RawForeignKey;
import org.db2code.rawmodel.RawTable;

@Slf4j
public class TableExtractor extends AbstractExtractor<DatabaseExtractionParameters> {

    private final ColumnExtractor columnExtractor = new ColumnExtractor();

    public List<RawTable> extract(
            DatabaseMetaData databaseMetaData, DatabaseExtractionParameters params) {
        try {
            return _extract(databaseMetaData, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<RawTable> _extract(
            DatabaseMetaData databaseMetaData, DatabaseExtractionParameters params)
            throws SQLException {
        List<RawTable> results = new ArrayList<>();
        try (ResultSet tables =
                databaseMetaData.getTables(
                        params.getCatalog(),
                        params.getSchemaPattern(),
                        params.getTableNamePattern(),
                        params.getTypes())) {
            while (tables.next()) {
                RawTable rawTable = new RawTable();
                for (TableMetadata mdItem : TableMetadata.values()) {
                    String mdValue = tryGetFromMetadata(mdItem, tables);
                    String propName =
                            JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(mdItem.getName());
                    setProperty(rawTable, mdValue, propName);
                }

                rawTable.setPrimaryKey(extractPrimaryKeys(databaseMetaData, rawTable));
                rawTable.setForeignKeys(extractForeignKeys(databaseMetaData, rawTable));
                setColumns(databaseMetaData, params, rawTable);
                results.add(rawTable);
            }
            return results;
        }
    }

    private void setColumns(
            DatabaseMetaData databaseMetaData,
            DatabaseExtractionParameters params,
            RawTable rawTable) {
        List<RawColumn> rawColumns =
                columnExtractor.extract(
                        databaseMetaData,
                        new DatabaseExtractionParameters(
                                params.getCatalog(),
                                params.getSchemaPattern(),
                                rawTable.getTableName(),
                                null,
                                null,
                                null,
                                false));
        if (!rawColumns.isEmpty()) {
            rawColumns.get(rawColumns.size() - 1).setIsLast(true);
        }
        rawTable.setColumns(rawColumns);
    }

    private List<RawForeignKey> extractForeignKeys(
            DatabaseMetaData databaseMetaData, RawTable rawTable) throws SQLException {
        List<RawForeignKey> fkResults = new ArrayList<>();
        try (ResultSet exportedKeys =
                databaseMetaData.getExportedKeys(
                        rawTable.getTableCat(),
                        rawTable.getTableSchem(),
                        rawTable.getTableName())) {
            RawForeignKey rawForeignKey = null;
            while (exportedKeys.next()) {
                rawForeignKey = new RawForeignKey();
                for (ExportedKeyMetadata mdItem : ExportedKeyMetadata.values()) {
                    Object mdValue = exportedKeys.getObject(mdItem.name());
                    String propName =
                            JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(mdItem.name());
                    setProperty(rawForeignKey, mdValue, propName);
                }
                fkResults.add(rawForeignKey);
            }

            if (rawForeignKey != null) {
                rawForeignKey.setIsLast(true);
            }
            return fkResults;
        }
    }

    private List<RawTable.RawPrimaryKey> extractPrimaryKeys(
            DatabaseMetaData databaseMetaData, RawTable table) throws SQLException {
        List<RawTable.RawPrimaryKey> results = new ArrayList<>();
        try (ResultSet primaryKeys =
                databaseMetaData.getPrimaryKeys(
                        table.getTableCat(), table.getTableSchem(), table.getTableName())) {
            RawTable.RawPrimaryKey rawPrimaryKey = null;
            while (primaryKeys.next()) {
                rawPrimaryKey = new RawTable.RawPrimaryKey();
                for (PrimaryKeyMetadata mdItem : PrimaryKeyMetadata.values()) {
                    String mdValue = primaryKeys.getString(mdItem.name());
                    String propName =
                            JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(mdItem.name());
                    setProperty(rawPrimaryKey, mdValue, propName);
                }
                results.add(rawPrimaryKey);
            }

            if (rawPrimaryKey != null) {
                rawPrimaryKey.setIsLast(true);
            }
            return results;
        }
    }
}
