package org.db2code.generator.java.pojo;

import lombok.RequiredArgsConstructor;
import org.db2code.MetadataExtractor;
import org.db2code.extractors.DatabaseExtractionParameters;
import org.db2code.rawmodel.RawDatabaseMetadata;

@RequiredArgsConstructor
public class DatabaseMetadataProvider implements MetadataProvider<DatabaseExtractionParameters> {

    private final MetadataExtractor metadataExtractor;

    @Override
    public RawDatabaseMetadata provide(DatabaseExtractionParameters extractionParameters) {
        return metadataExtractor.extract(extractionParameters);
    }
}
