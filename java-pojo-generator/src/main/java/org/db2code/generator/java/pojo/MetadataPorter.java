package org.db2code.generator.java.pojo;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.db2code.rawmodel.RawDatabaseMetadata;

@Slf4j
public class MetadataPorter {
    public void export(RawDatabaseMetadata rawDatabaseMetadata, String fileName) {
        if (isEmpty(fileName)) {
            fileName = UUID.randomUUID() + ".json";
        }
        try {
            log.info("Exporting to following file: " + fileName);
            File resultFile = new File(fileName);
            if (!resultFile.exists()) {
                log.info("File does not exists and will be created");
                File parentFile = resultFile.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                if (!resultFile.createNewFile()) {
                    throw new RuntimeException("Was unable to create file: " + fileName);
                }
            }
            createYamlOrJsonObjectMapper(resultFile).writeValue(resultFile, rawDatabaseMetadata);
            log.info("Metadata was successfully written to file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RawDatabaseMetadata importer(File file) {
        try {
            return createYamlOrJsonObjectMapper(file).readValue(file, RawDatabaseMetadata.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper createYamlOrJsonObjectMapper(File file) {
        String extension = FilenameUtils.getExtension(file.getName());
        ObjectMapper objectMapper;
        if (extension.equals("yml") || extension.endsWith("yaml")) {
            objectMapper = new ObjectMapper(new YAMLFactory());
        } else {
            objectMapper = new ObjectMapper();
        }
        objectMapper
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
}
