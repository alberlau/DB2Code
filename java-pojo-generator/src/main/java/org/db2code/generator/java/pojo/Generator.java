package org.db2code.generator.java.pojo;

import lombok.RequiredArgsConstructor;
import org.db2code.generator.java.pojo.adapter.ClassAdapter;
import org.db2code.generator.java.pojo.adapter.DatabaseAdapter;

@RequiredArgsConstructor
public class Generator {
    private final TemplatingProvider templatingProvider;

    public String generate(ClassAdapter classAdapter, String template) {
        return templatingProvider.merge(classAdapter, template);
    }

    public String generate(DatabaseAdapter databaseAdapter, String template) {
        return templatingProvider.merge(databaseAdapter, template);
    }
}
