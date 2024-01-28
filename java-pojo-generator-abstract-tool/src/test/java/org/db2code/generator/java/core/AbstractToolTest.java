package org.db2code.generator.java.core;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.db2code.generator.java.pojo.GeneratorStrategy;
import org.junit.jupiter.api.Test;

class AbstractToolTest {

    @Test
    public void testDbmlGeneration() throws URISyntaxException, IOException {
        MockedTool tool = new MockedTool();
        URL initUrl = AbstractToolTest.class.getResource("/init.sql");
        tool.setJdbcUrl(
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM '"
                        + new File(initUrl.toURI()).getAbsolutePath()
                        + "'");
        tool.setJdbcClassName("org.h2.Driver");
        Item param = new Item();
        param.setSchemaPattern("TEST_SCHEMA");
        param.setCatalog("TEST");
        param.setTableNamePattern("%");
        tool.setExtractionParameters(asList(param));
        URL baseDir = AbstractTool.class.getResource("/");
        tool.setBaseDir(new File(baseDir.toURI()).getAbsolutePath());
        tool.setGeneratorStrategy(GeneratorStrategy.SINGLE_FILE);
        tool.setTargetFolder("/");
        tool.setTargetPackage("test.dbml");
        tool.setSingleResultName("TEST");
        tool.setTypeMapFile("/type-mappings/dbml-type-map.properties");
        tool.setExt(".dbml");
        tool.setTemplates(asList("dbml.mustache"));

        tool.execute();

        String result =
                IOUtils.toString(
                        AbstractToolTest.class.getResourceAsStream("/test/dbml/TEST.dbml"));
        String expected =
                IOUtils.toString(AbstractToolTest.class.getResourceAsStream("/expected.dbml"));
        assertEquals(expected, result);
    }
}
