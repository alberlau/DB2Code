package testpkg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import test.constants.TestConstants;

public class ConstantsTest {
    @Test
    public void test() {
        assertEquals("testDatetime", TestConstants.TestTable1$testDatetime.getValue());
    }
}
