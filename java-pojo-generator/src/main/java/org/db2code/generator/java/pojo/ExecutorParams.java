package org.db2code.generator.java.pojo;

import java.util.Collection;
import org.db2code.extractors.ExtractionParameters;

public class ExecutorParams {
    private final Collection<ExtractionParameters> extractionParameters;
    private final Collection<String> templates;
    private final String targetPackage;
    private final String targetFolder;
    private final String baseDir;

    public ExecutorParams(
            Collection<ExtractionParameters> extractionParameters,
            Collection<String> templates,
            String targetPackage,
            String targetFolder,
            String baseDir) {
        this.extractionParameters = extractionParameters;
        this.templates = templates;
        this.targetPackage = targetPackage;
        this.targetFolder = targetFolder;
        this.baseDir = baseDir;
    }

    public Collection<ExtractionParameters> getExtractionParameters() {
        return extractionParameters;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public Collection<String> getTemplates() {
        return templates;
    }
}
