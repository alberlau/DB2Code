package org.db2code.generator.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.io.FilenameUtils;
import org.db2code.generator.java.pojo.TemplatingProvider;

public class FreemarkerTemplatingProvider implements TemplatingProvider {
    @Override
    public String merge(Object context, String templateLocation) {
        // Instantiating the Freemarker Configuration class
        Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        // Setting the directory for the template
        // Loading the template
        StringWriter out;
        try {
            System.out.println("-------------templateLocation: " + templateLocation);

            String fullPath =
                    FilenameUtils.getFullPathNoEndSeparator(
                            new File(new URI(templateLocation).getPath()).getAbsolutePath());
            System.out.println("-------------fullPath: " + fullPath);
            config.setDirectoryForTemplateLoading(new File(fullPath));
            String name = FilenameUtils.getName(templateLocation);
            System.out.println("-------------name: " + name);
            Template template = config.getTemplate(name);
            // Merging the template with the data model
            out = new StringWriter();
            template.process(context, out);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return out.toString();
    }
}
