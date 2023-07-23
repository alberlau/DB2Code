package org.db2code.generator.java.pojo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassWriter {
    public void write(String baseDir, String targetFolder, String pkg, String claz, String source) {

        FileWriter fw = null;
        try {
            Path path = Paths.get(baseDir, targetFolder, pkg.replaceAll("\\.", "/"));
            if (!path.toFile().exists() && !path.toFile().mkdirs()) {
                throw new RuntimeException("Dir cannot be created " + path);
            }
            File classFile = new File(path.toFile(), claz + ".java");
            if (classFile.exists()) {
                classFile.delete();
            }
            if (!classFile.createNewFile()) {
                throw new RuntimeException("File cannot be created " + classFile);
            }
            fw = new FileWriter(classFile, Charset.defaultCharset());
            fw.write(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(fw);
        }
    }

    private void close(FileWriter fw) {
        if (fw == null) {
            return;
        }
        try {
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
