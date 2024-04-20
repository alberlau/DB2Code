package testpkg;

import from_imported_metadata.testpkg.TestPrefTestTable1TestSuff;
import from_imported_metadata.testpkg.TestPrefTestTable2TestSuff;
import from_imported_metadata.testpkg.TestPrefTestTable3TestSuff;
import org.junit.jupiter.api.Test;

public class PrefixSuffixTest {
    @Test
    public void testPrefixSuffix() {
        new TestPrefTestTable1TestSuff();
        new TestPrefTestTable2TestSuff();
        new TestPrefTestTable3TestSuff();
    }
}
