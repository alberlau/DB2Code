package org.db2code.extractors;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.md.ExportedKeyMetadata;
import org.db2code.md.PrimaryKeyMetadata;
import org.db2code.md.TableMetadata;
import org.db2code.rawmodel.RawForeignKey;
import org.db2code.rawmodel.RawTable;

public class TableExtractor extends AbstractExtractor {
    public List<RawTable> extract(DatabaseMetaData databaseMetaData, ExtractionParameters params) {
        try {
            return _extract(databaseMetaData, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<RawTable> _extract(DatabaseMetaData databaseMetaData, ExtractionParameters params)
            throws SQLException {
        List<RawTable> results = new ArrayList<>();
        ResultSet tables =
                databaseMetaData.getTables(
                        params.getCatalog(),
                        params.getSchemaPattern(),
                        params.getTableNamePattern(),
                        params.getTypes());
        while (tables.next()) {
            RawTable rawTable = new RawTable();
            for (TableMetadata mdItem : TableMetadata.values()) {
                String mdValue = tables.getString(mdItem.name());
                String propName =
                        JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(mdItem.name());
                setProperty(rawTable, mdValue, propName);
            }

            rawTable.setPrimaryKey(extractPrimaryKeys(databaseMetaData, rawTable));
            rawTable.setForeignKeys(extractForeignKeys(databaseMetaData, rawTable));

            results.add(rawTable);
        }
        return results;
    }

    private List<RawForeignKey> extractForeignKeys(
            DatabaseMetaData databaseMetaData, RawTable rawTable) throws SQLException {
        List<RawForeignKey> fkResults = new ArrayList<>();
        ResultSet exportedKeys =
                databaseMetaData.getExportedKeys(
                        rawTable.getTableCat(), rawTable.getTableSchem(), rawTable.getTableName());
        while (exportedKeys.next()) {
            RawForeignKey rawPrimaryKey = new RawForeignKey();
            for (ExportedKeyMetadata mdItem : ExportedKeyMetadata.values()) {
                Object mdValue = exportedKeys.getObject(mdItem.name());
                String propName =
                        JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(mdItem.name());
                setProperty(rawPrimaryKey, mdValue, propName);
            }
            fkResults.add(rawPrimaryKey);
        }
        return fkResults;
    }

    private List<RawTable.RawPrimaryKey> extractPrimaryKeys(
            DatabaseMetaData databaseMetaData, RawTable table) throws SQLException {
        List<RawTable.RawPrimaryKey> results = new ArrayList<>();
        ResultSet primaryKeys =
                databaseMetaData.getPrimaryKeys(
                        table.getTableCat(), table.getTableSchem(), table.getTableName());
        while (primaryKeys.next()) {
            RawTable.RawPrimaryKey rawPrimaryKey = new RawTable.RawPrimaryKey();
            for (PrimaryKeyMetadata mdItem : PrimaryKeyMetadata.values()) {
                String mdValue = primaryKeys.getString(mdItem.name());
                String propName =
                        JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(mdItem.name());
                setProperty(rawPrimaryKey, mdValue, propName);
            }
            results.add(rawPrimaryKey);
        }
        return results;
    }
}
