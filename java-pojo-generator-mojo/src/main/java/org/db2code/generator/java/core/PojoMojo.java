package org.db2code.generator.java.core;

import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.db2code.generator.java.pojo.GeneratorStrategy;
import org.db2code.generator.java.pojo.adapter.DateImpl;

@Mojo(name = "generatePojo", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
@SuppressWarnings("PMD.DataClass")
public class PojoMojo extends AbstractMojo implements AbstractTool {
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

    @Parameter private GeneratorStrategy generatorStrategy;

    @Parameter private String singleResultName;

    @Parameter private String typeMapFile;

    @Override
    public void execute() {
        AbstractTool.super.execute();
    }

    @Override
    public List<Item> getExtractionParameters() {
        return extractionParameters;
    }

    @Override
    public String getJdbcClassName() {
        return jdbcClassName;
    }

    @Override
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public String getJdbcUser() {
        return jdbcUser;
    }

    @Override
    public String getJdbcPassword() {
        return jdbcPassword;
    }

    @Override
    public List<String> getTemplates() {
        return templates;
    }

    @Override
    public String getTargetPackage() {
        return targetPackage;
    }

    @Override
    public String getTargetFolder() {
        return targetFolder;
    }

    @Override
    public String getBaseDir() {
        return baseDir;
    }

    @Override
    public String getExt() {
        return ext;
    }

    @Override
    public DateImpl getDateImpl() {
        return dateImpl;
    }

    @Override
    public boolean isIncludeGenerationInfo() {
        return includeGenerationInfo;
    }

    @Override
    public GeneratorStrategy getGeneratorStrategy() {
        return generatorStrategy;
    }

    @Override
    public String getSingleResultName() {
        return singleResultName;
    }

    @Override
    public String getTypeMapFile() {
        return typeMapFile;
    }
}
