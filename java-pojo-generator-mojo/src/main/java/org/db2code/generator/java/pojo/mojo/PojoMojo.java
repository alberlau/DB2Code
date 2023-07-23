package org.db2code.generator.java.pojo.mojo;

import java.util.Collections;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.db2code.ConnectionProvider;
import org.db2code.MetadataExtractor;
import org.db2code.extractors.ExtractionParameters;
import org.db2code.generator.java.pojo.ClassWriter;
import org.db2code.generator.java.pojo.ExecutorParams;
import org.db2code.generator.java.pojo.Generator;
import org.db2code.generator.java.pojo.GeneratorExecutor;

@Mojo(name = "generatePojo", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class PojoMojo extends AbstractMojo {
    @Parameter private String jdbcClassName;

    @Parameter private String jdbcUrl;

    @Parameter private String jdbcUser;

    @Parameter private String jdbcPassword;

    @Parameter private List<ExtractionParameters> extractionParameters;

    @Parameter private List<String> templates;

    @Parameter private String targetPackage;

    @Parameter private String targetFolder;

    @Parameter private String baseDir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isWindows()) {
            jdbcUrl = jdbcUrl.replace("\\", "\\\\");
        }

        ConnectionProvider connectionProvider =
                new ConnectionProvider(
                        this.jdbcClassName, this.jdbcUrl, this.jdbcUser, this.jdbcPassword);
        MetadataExtractor metadataExtractor = new MetadataExtractor(connectionProvider);
        ClassWriter classWriter = new ClassWriter();
        Generator generator = new Generator();
        GeneratorExecutor generatorExecutor =
                new GeneratorExecutor(metadataExtractor, classWriter, generator);

        List<String> genTemplates = templates;
        if (genTemplates == null) {
            genTemplates = Collections.singletonList("pojo.mustache");
        }

        System.out.println(
                "------------- Generating to: " + baseDir + targetFolder + targetPackage);
        generatorExecutor.execute(
                new ExecutorParams(
                        this.extractionParameters,
                        genTemplates,
                        this.targetPackage,
                        this.targetFolder,
                        this.baseDir));
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }
}
