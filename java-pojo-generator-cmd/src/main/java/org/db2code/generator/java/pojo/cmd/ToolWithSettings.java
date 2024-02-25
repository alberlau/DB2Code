package org.db2code.generator.java.pojo.cmd;

import java.util.List;
import lombok.Data;
import org.db2code.generator.java.core.AbstractTool;
import org.db2code.generator.java.core.Item;
import org.db2code.generator.java.pojo.GeneratorStrategy;
import org.db2code.generator.java.pojo.adapter.DateImpl;

@Data
@SuppressWarnings({"PMD.DataClass", "PMD.TooManyFields"})
public class ToolWithSettings implements AbstractTool {

    private boolean includeGenerationInfo;
    private List<Item> extractionParameters;
    private String jdbcClassName;
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;
    private List<String> templates;
    private List<String> doNotGenerateTables;
    private String targetPackage;
    private String targetFolder;
    private String baseDir;
    private String ext;
    private DateImpl dateImpl;
    private GeneratorStrategy generatorStrategy;
    private String singleResultName;
    private String typeMapFile;
}
