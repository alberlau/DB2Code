package org.db2code.generator.java.pojo;

import java.util.Collection;
import org.db2code.extractors.ExtractionParameters;
import org.db2code.generator.java.pojo.adapter.DateImpl;

public class ExecutorParams {
    private final Collection<ExtractionParameters> extractionParameters;
    private final Collection<String> templates;
    private final GeneratorTarget generatorTarget;
    private final String ext;
    private final DateImpl dateImpl;
    private final boolean includeGenerationInfo;

    public ExecutorParams(
            Collection<ExtractionParameters> extractionParameters,
            Collection<String> templates,
            GeneratorTarget generatorTarget,
            String ext,
            DateImpl dateImpl,
            boolean includeGenerationInfo) {
        this.extractionParameters = extractionParameters;
        this.templates = templates;
        this.generatorTarget = generatorTarget;
        this.ext = ext;
        this.dateImpl = dateImpl;
        this.includeGenerationInfo = includeGenerationInfo;
    }

    public Collection<ExtractionParameters> getExtractionParameters() {
        return extractionParameters;
    }

    public Collection<String> getTemplates() {
        return templates;
    }

    public String getExt() {
        return ext;
    }

    public DateImpl getDateImpl() {
        return dateImpl;
    }

    public boolean isIncludeGenerationInfo() {
        return includeGenerationInfo;
    }

    public GeneratorTarget getGeneratorTarget() {
        return generatorTarget;
    }
}
