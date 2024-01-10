package org.db2code.extractors;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DatabaseExtractionParameters implements ExtractionParameters {
    private String catalog;
    private String schemaPattern;
    private String tableNamePattern;
    private String[] types;
    private String exportFile;
    private String procedureNamePattern;
    boolean includeStoredProcedures;
}
