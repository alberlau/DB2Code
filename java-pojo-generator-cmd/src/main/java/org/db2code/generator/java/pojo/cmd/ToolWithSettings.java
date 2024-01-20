package org.db2code.generator.java.pojo.cmd;

import java.util.List;
import lombok.Data;
import org.db2code.generator.java.pojo.adapter.DateImpl;
import org.db2code.generator.java.pojo.mojo.AbstractTool;
import org.db2code.generator.java.pojo.mojo.Item;

@SuppressWarnings("PMD.DataClass")
@Data
public class ToolWithSettings implements AbstractTool {

    private boolean includeGenerationInfo;
    private List<Item> extractionParameters;
    private String jdbcClassName;
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;
    private List<String> templates;
    private String targetPackage;
    private String targetFolder;
    private String baseDir;
    private String ext;
    private DateImpl dateImpl;
}
