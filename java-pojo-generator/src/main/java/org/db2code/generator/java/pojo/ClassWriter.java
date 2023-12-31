package org.db2code.generator.java.pojo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class ClassWriter {
    public void write(ExecutorParams params, String claz, String source) {

        FileWriter fw = null;
        try {
            GeneratorTarget generatorTarget = params.getGeneratorTarget();
            Path path = generatorTarget.getPath();
            String ext = params.getExt();
            File classFile = new File(path.toFile(), claz + (ext == null ? ".java" : ext));
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
