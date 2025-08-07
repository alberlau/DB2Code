package hibernate.testpkg;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {App.class})
@Transactional
public class HibernateGenerationTest {

    private final TestTable1Repository testTable1Repository;
    private final TestTable2Repository testTable2Repository;
    private final TestTable3Repository testTable3Repository;

    public HibernateGenerationTest(@Autowired TestTable1Repository testTable1Repository, @Autowired TestTable2Repository testTable2Repository, @Autowired TestTable3Repository testTable3Repository) {
        this.testTable1Repository = testTable1Repository;
        this.testTable2Repository = testTable2Repository;
        this.testTable3Repository = testTable3Repository;
    }

    @Test
    public void test() throws Exception {
        TestTable1 table1 = new TestTable1();
        table1.setTestVarchar("t1");
        table1 = testTable1Repository.save(table1);

        TestTable2 table2 = new TestTable2();
        table2.setTestVarchar("t2");
        table2 = testTable2Repository.save(table2);

        TestTable3 table3 = new TestTable3();
        table3.setTestTable2(table2);
        table3.setTestTable1(table1);
        table3 = testTable3Repository.save(table3);

        table2.setTestTable1(table1);
        table2 = testTable2Repository.save(table2);

        TestTable3 loadedTable3 = testTable3Repository.findById(table3.getSimpleId3()).get();
        assertEquals("t1", loadedTable3.getTestTable1().getTestVarchar());
        assertEquals("t2", loadedTable3.getTestTable2().getTestVarchar());
        assertEquals("t1", loadedTable3.getTestTable2().getTestTable1().getTestVarchar());
    }
}
