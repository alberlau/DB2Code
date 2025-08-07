package org.db2code.generator.java.pojo.adapter;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.db2code.extractors.DatabaseExtractionParameters;
import org.db2code.generator.java.pojo.ExecutorParams;
import org.db2code.generator.java.pojo.GeneratorTarget;
import org.db2code.rawmodel.RawDatabaseMetadata;
import org.junit.jupiter.api.Test;

class DatabaseAdapterTest {

    @Test
    public void test() throws IOException {
        RawDatabaseMetadata rawDatabaseMetadata = getRawDatabaseMetadata();
        ExecutorParams executorParams = getExecutorParams();

        DatabaseAdapter databaseAdapter = new DatabaseAdapter(rawDatabaseMetadata, executorParams);
        Map<String, DefaultClassAdapter> classes =
                databaseAdapter.getClasses().stream()
                        .collect(
                                Collectors.toMap(
                                        ClassAdapter::getClassName,
                                        claz -> (DefaultClassAdapter) claz));
        Map<String, PropertyAdapter> tt3Properties =
                classes.get("TestTable3").getProperties().stream()
                        .collect(Collectors.toMap(PropertyAdapter::getPropertyName, p -> p));
        for (PropertyAdapter tt1Property : tt3Properties.values()) {
            System.out.println(tt1Property);
        }
        System.out.println(classes);
    }

    private static RawDatabaseMetadata getRawDatabaseMetadata() throws IOException {
        byte[] mockMetadataBytes =
                DatabaseAdapterTest.class
                        .getResourceAsStream("/sample-metadata.json")
                        .readAllBytes();
        RawDatabaseMetadata rawDatabaseMetadata =
                new ObjectMapper()
                        .readValue(new String(mockMetadataBytes), RawDatabaseMetadata.class);
        return rawDatabaseMetadata;
    }

    private static ExecutorParams getExecutorParams() {
        String dir =
                Paths.get(
                                System.getProperty("java.io.tmpdir"),
                                "db2o-temp",
                                Long.toString(System.currentTimeMillis()))
                        .toString();

        ExecutorParams executorParams =
                new ExecutorParams(
                        Arrays.asList(
                                new DatabaseExtractionParameters(
                                        "testcat",
                                        "testSchemaPattern",
                                        "testTablePattern",
                                        new String[] {},
                                        null,
                                        null,
                                        false)),
                        Arrays.asList("pojo.mustache"),
                        null,
                        new GeneratorTarget("testpkg", "src", dir, null, null, null, null),
                        null,
                        DateImpl.UTIL_DATE,
                        null,
                        false);
        return executorParams;
    }
}
