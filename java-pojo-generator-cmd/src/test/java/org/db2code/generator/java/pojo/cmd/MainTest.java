package org.db2code.generator.java.pojo.cmd;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    public void testNonExistentFile() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> Main.main(new String[] {"non existent file"}));
        assertTrue(exception.getMessage().startsWith("File does not exists"));
    }

    @Test
    public void testEmptyFile() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> Main.main(new String[] {"src/test/resources/nullFile.json"}));
        assertTrue(exception.getCause() instanceof MismatchedInputException);
    }

    @Test
    public void testEmptyObject() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> Main.main(new String[] {"src/test/resources/emptyObject.json"}));
        assertEquals("No extractionParameters was set!", exception.getMessage());
    }

    @Test
    public void testMinimalJson() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> Main.main(new String[] {"src/test/resources/testMinimal.json"}));
        assertEquals("java.lang.ClassNotFoundException: org.h2.Driver", exception.getMessage());
    }

    @Test
    public void testYaml() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> Main.main(new String[] {"src/test/resources/testMinimal.yml"}));
        // Throwing ClassNotFoundException means that config was deserialized and process initiated,
        // but it is unable to find jdbc driver.
        assertEquals("java.lang.ClassNotFoundException: org.h2.Driver", exception.getMessage());
        exception =
                assertThrows(
                        RuntimeException.class,
                        () -> Main.main(new String[] {"src/test/resources/testMinimal.yaml"}));
        assertEquals("java.lang.ClassNotFoundException: org.h2.Driver", exception.getMessage());
    }
}
