package org.db2code.generator.java.pojo;

import java.io.File;
import org.db2code.extractors.MetadataFileExtractionParameters;
import org.db2code.rawmodel.RawDatabaseMetadata;

public class SerializedMetadataProvider
        implements MetadataProvider<MetadataFileExtractionParameters> {

    private final MetadataPorter metadataPorter = new MetadataPorter();

    @Override
    public RawDatabaseMetadata provide(MetadataFileExtractionParameters extractionParameters) {
        return metadataPorter.importer(new File(extractionParameters.getMetadataFile()));
    }
}
