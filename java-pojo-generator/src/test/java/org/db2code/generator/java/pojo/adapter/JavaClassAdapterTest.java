package org.db2code.generator.java.pojo.adapter;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Iterator;
import org.db2code.rawmodel.RawColumn;
import org.db2code.rawmodel.RawTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JavaClassAdapterTest {
    @Test
    public void testUniqueProperties() {
        RawTable rawTable = new RawTable();
        RawColumn col1 = new RawColumn();
        col1.setColumnName("test_col");
        RawColumn col2 = new RawColumn();
        col2.setColumnName("test_col_");
        rawTable.setColumns(asList(col1, col2));
        JavaClassAdapter classAdapter = new JavaClassAdapter(rawTable, null, null, false);
        Collection<JavaPropertyAdapter> properties = classAdapter.getProperties();
        Assertions.assertTrue(properties.size() == 2);
        Iterator<JavaPropertyAdapter> iterator = properties.iterator();
        JavaPropertyAdapter prop1 = iterator.next();
        Assertions.assertEquals("testCol", prop1.getPropertyName());
        Assertions.assertEquals("TestCol", prop1.getMethodName());
        JavaPropertyAdapter prop2 = iterator.next();
        Assertions.assertEquals("testCol1", prop2.getPropertyName());
        Assertions.assertEquals("TestCol1", prop2.getMethodName());
    }
}
