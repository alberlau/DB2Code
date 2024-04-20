package org.db2code.generator.java.pojo;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.IOException;
import java.io.StringWriter;

public class MustacheTemplatingProvider implements TemplatingProvider {
    public String merge(Object adapter, String template) {
        Mustache mustache = getMustache(template);
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
