package org.db2code.generator.java.pojo;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import lombok.extern.slf4j.Slf4j;
import org.db2code.MetadataExtractor;
import org.db2code.extractors.DatabaseExtractionParameters;
import org.db2code.extractors.ExtractionParameters;
import org.db2code.extractors.MetadataFileExtractionParameters;
import org.db2code.generator.java.pojo.adapter.ClassAdapter;
import org.db2code.generator.java.pojo.adapter.DatabaseAdapter;
import org.db2code.rawmodel.RawDatabaseMetadata;

@Slf4j
public class GeneratorExecutor {

    private final MetadataExtractor metadataExtractor;
    private final ClassWriter classWriter;
    private final Generator generator;
    private final SerializedMetadataProvider serializedMetadataProvider =
            new SerializedMetadataProvider();

    public GeneratorExecutor(
            MetadataExtractor metadataExtractor, ClassWriter classWriter, Generator generator) {
        this.metadataExtractor = metadataExtractor;
        this.classWriter = classWriter;
        this.generator = generator;
    }

    public void execute(ExecutorParams params) {
        params.getExtractionParameters()
                .forEach(
                        extractionParam -> {
                            RawDatabaseMetadata metadata = resolveMetadata(extractionParam);
                            if (metadata == null) {
                                return;
                            }

                            selectAndFollowGenerationStrategy(params, metadata);
                        });
    }

    private void selectAndFollowGenerationStrategy(
            ExecutorParams params, RawDatabaseMetadata metadata) {
        GeneratorStrategy generatorStrategy = params.getGeneratorTarget().getGeneratorStrategy();
        if (generatorStrategy == null || generatorStrategy == GeneratorStrategy.CLASS_PER_TABLE) {
            classPerTableSplitGeneration(params, metadata);
        } else if (generatorStrategy == GeneratorStrategy.SINGLE_FILE) {
            singleFileGeneration(params, metadata);
        } else {
            throw new RuntimeException("Unsupported generation strategy: " + generatorStrategy);
        }
    }

    private void singleFileGeneration(ExecutorParams params, RawDatabaseMetadata metadata) {
        params.getTemplates()
                .forEach(
                        template -> {
                            String result =
                                    generator.generate(
                                            new DatabaseAdapter(metadata, params), template);
                            classWriter.write(
                                    params,
                                    params.getGeneratorTarget().getSingleResultName(),
                                    result);
                        });
    }

    private void classPerTableSplitGeneration(ExecutorParams params, RawDatabaseMetadata metadata) {
        new DatabaseAdapter(metadata, params)
                .getClasses()
                .forEach(
                        claz ->
                                params.getTemplates()
                                        .forEach(
                                                template ->
                                                        generateSource(params, claz, template)));
    }

    private RawDatabaseMetadata resolveMetadata(ExtractionParameters param) {
        if (param instanceof DatabaseExtractionParameters) {
            if (metadataExtractor == null) {
                return null;
            }
            DatabaseExtractionParameters databaseExtractionParameters =
                    (DatabaseExtractionParameters) param;
            RawDatabaseMetadata extractedMetadata =
                    metadataExtractor.extract(databaseExtractionParameters);
            log.debug("Extracted database metadata is as follows: " + extractedMetadata);
            if (!isEmpty(databaseExtractionParameters.getExportFile())) {
                new MetadataPorter()
                        .export(extractedMetadata, databaseExtractionParameters.getExportFile());
                return null;
            } else {
                return extractedMetadata;
            }
        } else if (param instanceof MetadataFileExtractionParameters) {
            return serializedMetadataProvider.provide((MetadataFileExtractionParameters) param);
        } else {
            throw new RuntimeException("Unknown extractionType: " + param);
        }
    }

    private void generateSource(ExecutorParams params, ClassAdapter classAdapter, String template) {
        String source = generator.generate(classAdapter, template);
        classWriter.write(params, classAdapter.getClassName(), source);
    }
}
