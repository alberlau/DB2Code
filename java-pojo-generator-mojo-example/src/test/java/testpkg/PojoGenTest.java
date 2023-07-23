package testpkg;

import org.junit.jupiter.api.Test;
import pojo.testpkg.TestTable1;
import pojo.testpkg.TestTable2;
import pojo.testpkg.TestTable3;

public class PojoGenTest {
    @Test
    public void assertPojoGeneratedAndExists() {
        new TestTable1();
        new TestTable2();
        new TestTable3();
    }
}
