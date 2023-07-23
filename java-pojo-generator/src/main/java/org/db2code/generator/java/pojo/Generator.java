package org.db2code.generator.java.pojo;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.IOException;
import java.io.StringWriter;
import org.db2code.generator.java.pojo.adapter.JavaClassAdapter;

public class Generator {
    public String generate(JavaClassAdapter javaClassAdapter, String template) {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(template);
        StringWriter writer = new StringWriter();
        try {
            mustache.execute(writer, javaClassAdapter).flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }
}
