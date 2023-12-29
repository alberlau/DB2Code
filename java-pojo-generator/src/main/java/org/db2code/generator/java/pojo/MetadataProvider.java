package org.db2code.generator.java.pojo;

import org.db2code.extractors.ExtractionParameters;
import org.db2code.rawmodel.RawDatabaseMetadata;

public interface MetadataProvider<T extends ExtractionParameters> {
    RawDatabaseMetadata provide(T extractionParameters);
}
