package org.db2code.generator.java.pojo;

import org.db2code.MetadataExtractor;
import org.db2code.generator.java.pojo.adapter.DateImpl;
import org.db2code.generator.java.pojo.adapter.JavaClassAdapter;
import org.db2code.generator.java.pojo.adapter.JavaDatabaseAdapter;
import org.db2code.rawmodel.RawDatabaseMetadata;

public class GeneratorExecutor {

    private final MetadataExtractor metadataExtractor;
    private final ClassWriter classWriter;
    private final Generator generator;

    public GeneratorExecutor(
            MetadataExtractor metadataExtractor, ClassWriter classWriter, Generator generator) {
        this.metadataExtractor = metadataExtractor;
        this.classWriter = classWriter;
        this.generator = generator;
    }

    public void execute(ExecutorParams params) {
        params.getExtractionParameters()
                .forEach(
                        param -> {
                            RawDatabaseMetadata metadata = metadataExtractor.extract(param);

                            DateImpl dateImpl = params.getDateImpl();
                            if (dateImpl == null) {
                                dateImpl = DateImpl.UTIL_DATE;
                            }

                            new JavaDatabaseAdapter(metadata, params.getTargetPackage(), dateImpl)
                                    .getClasses()
                                    .forEach(
                                            javaClass ->
                                                    params.getTemplates()
                                                            .forEach(
                                                                    template ->
                                                                            generateSource(
                                                                                    params,
                                                                                    javaClass,
                                                                                    template)));
                        });
    }

    private void generateSource(
            ExecutorParams params, JavaClassAdapter javaClass, String template) {
        String source = generator.generate(javaClass, template);
        classWriter.write(
                params.getBaseDir(),
                params.getTargetFolder(),
                params.getTargetPackage(),
                javaClass.getClassName(),
                source,
                params.getExt());
    }
}
