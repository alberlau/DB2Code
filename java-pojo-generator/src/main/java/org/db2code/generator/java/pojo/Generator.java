package org.db2code.generator.java.pojo;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.IOException;
import java.io.StringWriter;
import org.db2code.generator.java.pojo.adapter.ClassAdapter;
import org.db2code.generator.java.pojo.adapter.DatabaseAdapter;

public class Generator {
    public String generate(ClassAdapter classAdapter, String template) {
        Mustache mustache = getMustache(template);
        return merge(classAdapter, mustache);
    }

    public String generate(DatabaseAdapter databaseAdapter, String template) {
        Mustache mustache = getMustache(template);
        return merge(databaseAdapter, mustache);
    }

    private static String merge(Object adapter, Mustache mustache) {
        StringWriter writer = new StringWriter();
        try {
            mustache.execute(writer, adapter).flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    private static Mustache getMustache(String template) {
        MustacheFactory mf = new DefaultMustacheFactory();
        return mf.compile(template);
    }
}
