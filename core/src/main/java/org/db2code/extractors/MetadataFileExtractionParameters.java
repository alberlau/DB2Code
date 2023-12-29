package org.db2code.extractors;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MetadataFileExtractionParameters implements ExtractionParameters {
    private final String metadataFile;
}
