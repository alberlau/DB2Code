package org.db2code.generator.java.core;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.db2code.ConnectionProvider;
import org.db2code.MetadataExtractor;
import org.db2code.extractors.DatabaseExtractionParameters;
import org.db2code.extractors.ExtractionParameters;
import org.db2code.extractors.MetadataFileExtractionParameters;
import org.db2code.generator.java.pojo.*;
import org.db2code.generator.java.pojo.adapter.DateImpl;

public interface AbstractTool {

    default void execute() {
        _execute();
    }

    private void _execute() {
        String jdbcUrl = getJdbcUrl();
        if (!isEmpty(getJdbcUrl()) && isWindows()) {
            jdbcUrl = jdbcUrl.replace("\\", "\\\\");
        }

        MetadataExtractor metadataExtractor = null;
        @SuppressWarnings("PMD.CloseResource")
        ConnectionProvider connectionProvider = null;
        try {
            if (!isEmpty(jdbcUrl) && !isEmpty(getJdbcClassName())) {
                connectionProvider =
                        new ConnectionProvider(
                                getJdbcClassName(), jdbcUrl, getJdbcUser(), getJdbcPassword());
                metadataExtractor = new MetadataExtractor(connectionProvider);
            }
            ClassWriter classWriter = new ClassWriter();
            Generator generator = new Generator();
            GeneratorExecutor generatorExecutor =
                    new GeneratorExecutor(metadataExtractor, classWriter, generator);

            List<String> genTemplates = getTemplates();
            if (genTemplates == null) {
                genTemplates = Collections.singletonList("pojo.mustache");
            }

            generatorExecutor.execute(
                    new ExecutorParams(
                            mapExtractionParameters(),
                            genTemplates,
                            getDoNotGenerateTables(),
                            new GeneratorTarget(
                                    getTargetPackage(),
                                    getTargetFolder(),
                                    getBaseDir(),
                                    getSingleResultName(),
                                    getGeneratorStrategy(),
                                    getPrefix(),
                                    getSuffix()),
                            getExt(),
                            getDateImpl(),
                            getTypeMapFile(),
                            isIncludeGenerationInfo()));
        } finally {
            closeConnectionProviderIfNeeded(connectionProvider);
        }
    }

    private static void closeConnectionProviderIfNeeded(ConnectionProvider connectionProvider) {
        if (connectionProvider != null) {
            try {
                connectionProvider.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    List<Item> getExtractionParameters();

    default List<ExtractionParameters> mapExtractionParameters() {
        List<Item> extractionParameters = getExtractionParameters();
        if (extractionParameters == null || extractionParameters.isEmpty()) {
            throw new RuntimeException("No extractionParameters was set!");
        }
        return extractionParameters.stream()
                .map(
                        item -> {
                            if (!isEmpty(item.getImportFile())) {
                                if (!isEmpty(item.getCatalog())
                                        || !isEmpty(item.getSchemaPattern())
                                        || !isEmpty(item.getTableNamePattern())
                                        || item.getTypes() != null && item.getTypes().length > 0) {
                                    throw new RuntimeException(
                                            "If importFile is specified, no database related parameters should be specified. Metadata is loaded from file specified!");
                                }
                                return new MetadataFileExtractionParameters(item.getImportFile());
                            } else {
                                if (isEmpty(getJdbcClassName()) || isEmpty(getJdbcUrl())) {
                                    throw new RuntimeException(
                                            "If no importFile is specified, at least jdbcClassName and jdbcUrl should be specified.");
                                }
                                return new DatabaseExtractionParameters(
                                        item.getCatalog(),
                                        item.getSchemaPattern(),
                                        item.getTableNamePattern(),
                                        item.getTypes(),
                                        item.getExportFile(),
                                        item.getProcedureNamePattern(),
                                        item.isIncludeStoredProcedures());
                            }
                        })
                .collect(Collectors.toList());
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    String getJdbcClassName();

    String getJdbcUrl();

    String getJdbcUser();

    String getJdbcPassword();

    List<String> getTemplates();

    List<String> getDoNotGenerateTables();

    String getTargetPackage();

    String getTargetFolder();

    String getBaseDir();

    String getExt();

    DateImpl getDateImpl();

    boolean isIncludeGenerationInfo();

    GeneratorStrategy getGeneratorStrategy();

    String getSingleResultName();

    String getTypeMapFile();

    String getPrefix();

    String getSuffix();
}
