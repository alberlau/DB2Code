package org.db2code.generator.java.pojo.cmd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

@Slf4j
public final class Main {

    private Main() {}

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
        File file = getFile(args);
        ObjectMapper objectMapper = createYamlOrJsonObjectMapper(file);
        ToolWithSettings toolWithSettings = loadToolAndSettings(objectMapper, file);

        toolWithSettings.execute();

        log.info("Generation was successful!");
    }

    private static ToolWithSettings loadToolAndSettings(ObjectMapper objectMapper, File file) {
        try {
            return objectMapper.readValue(file, ToolWithSettings.class);
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
        return objectMapper;
    }

    private static File getFile(String[] args) {
        String settingsFile = "settings.yml";
        if (args.length > 0) {
            settingsFile = args[0];
        }
        return locateFile(settingsFile);
    }

    private static File locateFile(String fileName) {
        if (fileName == null) {
            throw new RuntimeException("File to locate must be specified!");
        }
        File file = new File(fileName);
        if (!file.isAbsolute()) {
            file = new File(Paths.get(System.getProperty("user.dir")).toFile(), fileName);
        }
        if (!file.exists()) {
            throw new RuntimeException("File does not exists: " + file);
        }
        return file;
    }
}
