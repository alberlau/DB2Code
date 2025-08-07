package hibernate.testpkg;

import jakarta.persistence.*;

@Entity
@Table(name = "TEST_TABLE_3")
public class TestTable3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer simpleId3;

    @ManyToOne
    @JoinColumn(name = "simple_id_1")
    private TestTable1 testTable1;

    @ManyToOne
    @JoinColumn(name = "simple_id_2")
    private TestTable2 testTable2;

    public Integer getSimpleId3() {
        return simpleId3;
    }

    public void setSimpleId3(Integer simpleId3) {
        this.simpleId3 = simpleId3;
    }

    public TestTable1 getTestTable1() {
        return testTable1;
    }

    public void setTestTable1(TestTable1 testTable1) {
        this.testTable1 = testTable1;
    }

    public TestTable2 getTestTable2() {
        return testTable2;
    }

    public void setTestTable2(TestTable2 testTable2) {
        this.testTable2 = testTable2;
    }
}
