package org.db2code.generator.java.core;

import java.util.List;
import org.db2code.generator.java.pojo.GeneratorStrategy;
import org.db2code.generator.java.pojo.adapter.DateImpl;

public class MockedTool implements AbstractTool {
    private List<Item> extractionParameters;
    private String jdbcClassName;
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;
    private List<String> templates;
    private String targetPackage;
    private String targetFolder;
    private String baseDir;
    private String ext;
    private DateImpl dateImpl;
    private boolean includeGenerationInfo;
    private GeneratorStrategy generatorStrategy;
    private String singleResultName;
    private String typeMapFile;

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

    public void setExtractionParameters(List<Item> extractionParameters) {
        this.extractionParameters = extractionParameters;
    }

    public void setJdbcClassName(String jdbcClassName) {
        this.jdbcClassName = jdbcClassName;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void setJdbcUser(String jdbcUser) {
        this.jdbcUser = jdbcUser;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public void setTemplates(List<String> templates) {
        this.templates = templates;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public void setDateImpl(DateImpl dateImpl) {
        this.dateImpl = dateImpl;
    }

    public void setIncludeGenerationInfo(boolean includeGenerationInfo) {
        this.includeGenerationInfo = includeGenerationInfo;
    }

    public void setGeneratorStrategy(GeneratorStrategy generatorStrategy) {
        this.generatorStrategy = generatorStrategy;
    }

    public void setSingleResultName(String singleResultName) {
        this.singleResultName = singleResultName;
    }

    public void setTypeMapFile(String typeMapFile) {
        this.typeMapFile = typeMapFile;
    }
}
