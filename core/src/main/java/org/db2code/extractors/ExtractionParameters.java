package org.db2code.extractors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtractionParameters {
    private String catalog;
    private String schemaPattern;
    private String tableNamePattern;
    private String[] types;
}
