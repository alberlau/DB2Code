package testpkg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import pojo.testpkg.TestTable1;
import pojo.testpkg.TestTable2;
import pojo.testpkg.TestTable3;

public class PojoGenTest {
    @Test
    public void assertPojoGeneratedAndExists() throws NoSuchFieldException {
        TestTable1 testTable1 = new TestTable1();
        assertEquals(
                "java.util.Date",
                testTable1.getClass().getDeclaredField("testDate").getType().getName());
        new TestTable2();
        new TestTable3();
    }

    @Test
    public void assertCustomGeneratorJsonExists() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readTree(PojoGenTest.class.getResourceAsStream("/TestTable1.json"));
        objectMapper.readTree(PojoGenTest.class.getResourceAsStream("/TestTable2.json"));
        objectMapper.readTree(PojoGenTest.class.getResourceAsStream("/TestTable3.json"));
    }

    @Test
    public void testFromImportedMetadataExists() {
        new from_imported_metadata.testpkg.TestTable1();
        new from_imported_metadata.testpkg.TestTable2();
        new from_imported_metadata.testpkg.TestTable3();
    }

    @Test
    public void testGeneratedJson() throws IOException {
        new ObjectMapper(new YAMLFactory())
                .readTree(PojoGenTest.class.getResourceAsStream("/TEST_SCHEMA.yml"));
    }

    @Test
    public void testDoNotGenerateWorks() throws ClassNotFoundException {
        Class.forName("pojo.testpkg.doNotMatch.TestTable1");
        Class.forName("pojo.testpkg.doNotMatch.TestTable2");
        try {
            Class.forName("pojo.testpkg.doNotMatch.TestTable3");
            fail("Class should not be generated.");
        } catch (ClassNotFoundException e) {
            assertEquals("pojo.testpkg.doNotMatch.TestTable3", e.getMessage());
        }
    }
}
