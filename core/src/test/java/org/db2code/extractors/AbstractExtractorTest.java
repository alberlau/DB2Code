package org.db2code.extractors;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.DatabaseMetaData;
import java.util.List;
import org.db2code.rawmodel.AbstractRawItem;
import org.db2code.rawmodel.RawColumn;
import org.junit.jupiter.api.Test;

class AbstractExtractorTest {

    @Test
    public void testSetPropertyDifferentTypes() {
        MockImpl mock = new MockImpl();
        RawColumn rawColumn = new RawColumn();
        Short shortOne = Short.valueOf("1");
        mock.setProperty(rawColumn, shortOne, "columnSize");
        assertEquals(1, rawColumn.getColumnSize());
        mock.setProperty(rawColumn, Integer.MIN_VALUE, "columnSize");
        assertEquals(Integer.MIN_VALUE, rawColumn.getColumnSize());
        mock.setProperty(rawColumn, Byte.MIN_VALUE, "columnSize");
        assertEquals(Byte.MIN_VALUE, rawColumn.getColumnSize());
    }

    public static class MockImpl extends AbstractExtractor {

        @Override
        public List<? extends AbstractRawItem> extract(
                DatabaseMetaData databaseMetaData, ExtractionParameters params) {
            return null;
        }
    }
}
