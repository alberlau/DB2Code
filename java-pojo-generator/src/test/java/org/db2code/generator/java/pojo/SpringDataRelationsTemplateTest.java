package org.db2code.generator.java.pojo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.db2code.MetadataExtractor;
import org.db2code.extractors.DatabaseExtractionParameters;
import org.db2code.generator.java.pojo.adapter.DateImpl;
import org.db2code.rawmodel.RawColumn;
import org.db2code.rawmodel.RawDatabaseMetadata;
import org.db2code.rawmodel.RawForeignKey;
import org.db2code.rawmodel.RawTable;
import org.db2code.rawmodel.RawTable.RawPrimaryKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SpringDataRelationsTemplateTest {

    @Test
    void testRelations() throws Exception {
        RawDatabaseMetadata metadata = buildMetadata();
        MetadataExtractor metadataExtractor = mock(MetadataExtractor.class);
        when(metadataExtractor.extract(any())).thenReturn(metadata);
        GeneratorExecutor generatorExecutor =
                new GeneratorExecutor(
                        metadataExtractor,
                        new ClassWriter(),
                        new Generator(new MustacheTemplatingProvider()));
        String dir =
                Paths.get(
                                System.getProperty("java.io.tmpdir"),
                                "db2o-temp",
                                Long.toString(System.currentTimeMillis()))
                        .toString();
        Assertions.assertTrue(new File(dir).mkdirs());
        try {
            generatorExecutor.execute(
                    new ExecutorParams(
                            List.of(
                                    new DatabaseExtractionParameters(
                                            "cat",
                                            "schem",
                                            "%",
                                            new String[] {},
                                            null,
                                            null,
                                            false)),
                            Arrays.asList("spring-data.mustache"),
                            null,
                            new GeneratorTarget("testpkg", "src", dir, null, null, null, null),
                            null,
                            DateImpl.UTIL_DATE,
                            null,
                            false));
            String classA =
                    FileUtils.readFileToString(
                            Paths.get(dir, "src", "testpkg", "TableA.java").toFile(),
                            Charset.defaultCharset());
            String classB =
                    FileUtils.readFileToString(
                            Paths.get(dir, "src", "testpkg", "TableB.java").toFile(),
                            Charset.defaultCharset());
            String classC =
                    FileUtils.readFileToString(
                            Paths.get(dir, "src", "testpkg", "TableC.java").toFile(),
                            Charset.defaultCharset());
            Assertions.assertTrue(classA.contains("@OneToMany"));
            Assertions.assertTrue(classB.contains("@ManyToOne"));
            Assertions.assertTrue(classA.contains("@ManyToMany"));
            Assertions.assertTrue(classC.contains("@ManyToMany"));
        } finally {
            FileUtils.deleteDirectory(new File(dir));
        }
    }

    private RawDatabaseMetadata buildMetadata() {
        RawTable tableA = createTable("TABLE_A", "A_ID");
        RawTable tableB = createTable("TABLE_B", "B_ID");
        RawTable tableC = createTable("TABLE_C", "C_ID");
        RawTable join = createJoinTable("TABLE_A_C", "A_ID", tableA, "C_ID", tableC);

        tableA.setForeignKeys(
                List.of(fk(tableA, "A_ID", tableB, "A_ID"), fk(tableA, "A_ID", join, "A_ID")));
        tableB.setImportedKeys(List.of(fk(tableA, "A_ID", tableB, "A_ID")));
        tableC.setForeignKeys(List.of(fk(tableC, "C_ID", join, "C_ID")));
        join.setImportedKeys(
                List.of(fk(tableA, "A_ID", join, "A_ID"), fk(tableC, "C_ID", join, "C_ID")));

        RawDatabaseMetadata metadata = new RawDatabaseMetadata();
        metadata.setTables(Arrays.asList(tableA, tableB, tableC, join));
        return metadata;
    }

    private RawTable createTable(String name, String idCol) {
        RawTable table = new RawTable();
        table.setTableName(name);
        table.setTableType("TABLE");
        RawColumn col = createColumn(name, idCol);
        table.setColumns(List.of(col));
        RawPrimaryKey pk = new RawPrimaryKey();
        pk.setColumnName(idCol);
        table.setPrimaryKey(List.of(pk));
        return table;
    }

    private RawTable createJoinTable(
            String name, String col1, RawTable ref1, String col2, RawTable ref2) {
        RawTable table = new RawTable();
        table.setTableName(name);
        table.setTableType("TABLE");
        RawColumn c1 = createColumn(name, col1);
        RawColumn c2 = createColumn(name, col2);
        table.setColumns(List.of(c1, c2));
        RawPrimaryKey pk1 = new RawPrimaryKey();
        pk1.setColumnName(col1);
        RawPrimaryKey pk2 = new RawPrimaryKey();
        pk2.setColumnName(col2);
        table.setPrimaryKey(Arrays.asList(pk1, pk2));
        return table;
    }

    private RawColumn createColumn(String tableName, String columnName) {
        RawColumn col = new RawColumn();
        col.setTableName(tableName);
        col.setColumnName(columnName);
        col.setDataType(4);
        col.setTypeName("INTEGER");
        col.setColumnSize(32);
        col.setNullable(0);
        col.setIsNullable("NO");
        return col;
    }

    private RawForeignKey fk(RawTable pkTable, String pkCol, RawTable fkTable, String fkCol) {
        RawForeignKey fk = new RawForeignKey();
        fk.setPktableName(pkTable.getTableName());
        fk.setPkcolumnName(pkCol);
        fk.setFktableName(fkTable.getTableName());
        fk.setFkcolumnName(fkCol);
        return fk;
    }
}
