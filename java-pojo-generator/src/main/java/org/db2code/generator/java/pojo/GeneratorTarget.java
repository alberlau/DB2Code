package org.db2code.generator.java.pojo;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Data;

@Data
public class GeneratorTarget {
    private final String targetPackage;
    private final String targetFolder;
    private final String baseDir;

    public Path getPath() {
        String pkg = getTargetPackage();
        if (pkg == null) {
            pkg = "";
        }
        String targetFolder = getTargetFolder();
        if (targetFolder == null) {
            targetFolder = ".";
        }
        String baseDir = getBaseDir();
        if (isBlank(baseDir)) {
            throw new RuntimeException("baseDir is required");
        }
        Path path = Paths.get(baseDir, targetFolder, pkg.replaceAll("\\.", "/"));
        if (!path.toFile().exists() && !path.toFile().mkdirs()) {
            throw new RuntimeException("Dir cannot be created " + path);
        }
        return path;
    }
}
