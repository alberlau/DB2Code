package org.db2code;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import org.db2code.extractors.DatabaseExtractionParameters;
import org.db2code.rawmodel.RawDatabaseMetadata;
import org.h2.util.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MetadataExtractorTest {

    private static ObjectMapper objectMapper;
    static MetadataExtractor metadataExtractor;

    @BeforeAll
    public static void beforeAll() throws IOException, SQLException {
        ConnectionProvider connectionProvider =
                new ConnectionProvider(
                        "org.h2.Driver", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", null, null);

        PreparedStatement preparedStatement =
                connectionProvider
                        .getConnection()
                        .prepareStatement(
                                new String(
                                        IOUtils.readBytesAndClose(
                                                Objects.requireNonNull(
                                                        MetadataExtractorTest.class
                                                                .getResourceAsStream(
                                                                        "/generic-case/sample.sql")),
                                                -1)));
        preparedStatement.execute();

        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        metadataExtractor = new MetadataExtractor(connectionProvider);
    }

    @Test
    public void genericTest() throws IOException {
        DatabaseExtractionParameters extractionParameters =
                new DatabaseExtractionParameters(
                        "TEST", "TEST_SCHEMA", null, null, null, null, false);
        RawDatabaseMetadata metadata = metadataExtractor.extract(extractionParameters);

        JsonNode expected =
                objectMapper.readTree(
                        MetadataExtractorTest.class.getResourceAsStream(
                                "/generic-case/sample-assert.json"));
        Assertions.assertEquals(
                expected, objectMapper.readTree(objectMapper.writeValueAsString(metadata)));
    }

    @Test
    public void noFiltersTest() {
        RawDatabaseMetadata metadata =
                metadataExtractor.extract(new DatabaseExtractionParameters());
        Assertions.assertTrue(
                metadata.getTables().size() > 3,
                "Tables more than we defined. Should include system tables");
        Assertions.assertTrue(
                metadata.getTables().stream()
                                .mapToLong(rawTable -> rawTable.getColumns().size())
                                .sum()
                        > 11,
                "Columns more than we defined. Should include system columns");
    }

    @Test
    public void tableFilterTest() throws IOException {
        DatabaseExtractionParameters extractionParameters =
                DatabaseExtractionParameters.builder().tableNamePattern("TEST_TABLE_1").build();
        RawDatabaseMetadata metadata = metadataExtractor.extract(extractionParameters);

        JsonNode expected =
                objectMapper.readTree(
                        MetadataExtractorTest.class.getResourceAsStream(
                                "/generic-case/table-filter.json"));
        Assertions.assertEquals(
                expected, objectMapper.readTree(objectMapper.writeValueAsString(metadata)));
    }
}
