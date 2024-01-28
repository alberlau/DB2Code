package org.db2code.generator.java.pojo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.db2code.MetadataExtractor;
import org.db2code.extractors.DatabaseExtractionParameters;
import org.db2code.generator.java.pojo.adapter.DateImpl;
import org.db2code.rawmodel.RawDatabaseMetadata;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeneratorExecutorTest {

    public static final String TESTPKG = "testpkg";
    public static final String TARGET_FOLDER = "src";
    private String dir;

    @BeforeEach
    public void init() {
        dir =
                Paths.get(
                                System.getProperty("java.io.tmpdir"),
                                "db2o-temp",
                                Long.toString(System.currentTimeMillis()))
                        .toString();
        Assertions.assertTrue(new File(dir).mkdirs());
    }

    @AfterEach
    public void clean() throws IOException {
        FileUtils.deleteDirectory(new File(dir));
    }

    @Test
    public void test() throws IOException {
        MetadataExtractor metadataExtractor = mock(MetadataExtractor.class);
        GeneratorExecutor generatorExecutor =
                new GeneratorExecutor(metadataExtractor, new ClassWriter(), new Generator());
        byte[] mockMetadataBytes =
                GeneratorExecutorTest.class
                        .getResourceAsStream("/sample-metadata.json")
                        .readAllBytes();
        RawDatabaseMetadata rawDatabaseMetadata =
                new ObjectMapper()
                        .readValue(new String(mockMetadataBytes), RawDatabaseMetadata.class);
        when(metadataExtractor.extract(any())).thenReturn(rawDatabaseMetadata);
        generatorExecutor.execute(
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
                        new GeneratorTarget(TESTPKG, TARGET_FOLDER, dir, null, null),
                        null,
                        DateImpl.UTIL_DATE,
                        null,
                        false));

        File[] files = Paths.get(dir, TARGET_FOLDER, TESTPKG).toFile().listFiles();
        Assertions.assertTrue(files.length > 0);
        Arrays.stream(Objects.requireNonNull(files))
                .forEach(
                        file -> {
                            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                                String result =
                                        IOUtils.toString(fileInputStream, Charset.defaultCharset());
                                String expected =
                                        new String(
                                                GeneratorExecutorTest.class
                                                        .getResourceAsStream(
                                                                "/generic-test-case-assertion/"
                                                                        + file.getName())
                                                        .readAllBytes());
                                Assertions.assertEquals(expected, result);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
    }
}
