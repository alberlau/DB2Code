package org.db2code.generator.java.pojo;

import java.util.Collection;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.db2code.extractors.ExtractionParameters;
import org.db2code.generator.java.pojo.adapter.DateImpl;

@Data
@RequiredArgsConstructor
public class ExecutorParams {
    private final Collection<ExtractionParameters> extractionParameters;
    private final Collection<String> templates;
    private final GeneratorTarget generatorTarget;
    private final String ext;
    private final DateImpl dateImpl;
    private final boolean includeGenerationInfo;
}
