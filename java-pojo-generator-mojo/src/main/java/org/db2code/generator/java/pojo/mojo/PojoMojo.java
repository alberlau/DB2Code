package org.db2code.generator.java.pojo.mojo;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.db2code.ConnectionProvider;
import org.db2code.MetadataExtractor;
import org.db2code.extractors.DatabaseExtractionParameters;
import org.db2code.extractors.ExtractionParameters;
import org.db2code.extractors.MetadataFileExtractionParameters;
import org.db2code.generator.java.pojo.*;
import org.db2code.generator.java.pojo.adapter.DateImpl;

@Mojo(name = "generatePojo", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class PojoMojo extends AbstractMojo {
    @Parameter private String jdbcClassName;

    @Parameter private String jdbcUrl;

    @Parameter private String jdbcUser;

    @Parameter private String jdbcPassword;

    @Parameter private List<Item> extractionParameters;

    @Parameter private List<String> templates;

    @Parameter private String targetPackage;

    @Parameter private String targetFolder;

    @Parameter private String baseDir;

    @Parameter private String ext;

    @Parameter private DateImpl dateImpl;
    @Parameter private boolean includeGenerationInfo;
    private ConnectionProvider connectionProvider = null;

    public void execute() {
        try {
            _execute();
        } finally {
            closeConnectionProvider();
        }
    }

    private void closeConnectionProvider() {
        try {
            if (connectionProvider != null) {
                connectionProvider.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void _execute() {
        if (!isEmpty(jdbcUrl) && isWindows()) {
            jdbcUrl = jdbcUrl.replace("\\", "\\\\");
        }

        MetadataExtractor metadataExtractor = null;
        if (!isEmpty(jdbcUrl) && !isEmpty(jdbcClassName)) {
            connectionProvider =
                    new ConnectionProvider(
                            this.jdbcClassName, this.jdbcUrl, this.jdbcUser, this.jdbcPassword);
            metadataExtractor = new MetadataExtractor(connectionProvider);
        }
        ClassWriter classWriter = new ClassWriter();
        Generator generator = new Generator();
        GeneratorExecutor generatorExecutor =
                new GeneratorExecutor(metadataExtractor, classWriter, generator);

        List<String> genTemplates = templates;
        if (genTemplates == null) {
            genTemplates = Collections.singletonList("pojo.mustache");
        }

        generatorExecutor.execute(
                new ExecutorParams(
                        getExtractionParameters(),
                        genTemplates,
                        new GeneratorTarget(this.targetPackage, this.targetFolder, this.baseDir),
                        ext,
                        dateImpl,
                        includeGenerationInfo));
    }

    private List<ExtractionParameters> getExtractionParameters() {
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
                                return new DatabaseExtractionParameters(
                                        item.getCatalog(),
                                        item.getSchemaPattern(),
                                        item.getTableNamePattern(),
                                        item.getTypes(),
                                        item.getExportFile());
                            }
                        })
                .collect(Collectors.toList());
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }
}
