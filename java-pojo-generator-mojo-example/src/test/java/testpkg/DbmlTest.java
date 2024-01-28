package testpkg;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class DbmlTest {
    @Test
    public void testDbmlGenerated() {
        assertNotNull(DbmlTest.class.getResourceAsStream("/test/dbml/TEST.dbml"));
    }
}
