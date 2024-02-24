package org.db2code.generator.java.pojo.adapter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class SqlTypeMapperTest {
    @Test
    public void testNonExistingTypeMap() {
        try {
            new SqlTypeMapper(DateImpl.LOCAL_DATE, "/somefile.txt");
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals(
                    "Was unable to find TypeMap nor in classpath nor in file system.",
                    e.getMessage());
        }
    }

    @Test
    public void testTypeMapInClassPath() {
        SqlTypeMapper mapper =
                new SqlTypeMapper(DateImpl.LOCAL_DATE, "/type-mappings/java-type-map.properties");
        assertEquals("String", mapper.getMappedType(1));
    }

    @Test
    public void testTypeMapInFileSystem() throws URISyntaxException {
        java.net.URL resource =
                getClass().getClassLoader().getResource("some-fs-type-map.properties");
        SqlTypeMapper mapper =
                new SqlTypeMapper(
                        DateImpl.LOCAL_DATE, new File(resource.toURI()).getAbsolutePath());
        assertEquals("some-custom-type", mapper.getMappedType(12));
    }
}
