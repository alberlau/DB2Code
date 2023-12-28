package org.db2code.extractors;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseExtractionParameters implements ExtractionParameters {
    private String catalog;
    private String schemaPattern;
    private String tableNamePattern;
    private String[] types;
    private String exportFile;
}
