package org.db2code.generator.java.pojo;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.db2code.rawmodel.RawDatabaseMetadata;

@Slf4j
public class MetadataPorter {
    private static final ObjectMapper objectMapper =
            new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .enable(SerializationFeature.INDENT_OUTPUT);

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
            objectMapper.writeValue(resultFile, rawDatabaseMetadata);
            log.info("Metadata was successfully written to file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RawDatabaseMetadata importer(File file) {
        try {
            return objectMapper.readValue(file, RawDatabaseMetadata.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
