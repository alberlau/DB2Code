package hibernate.testpkg;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "TEST_TABLE_2")
public class TestTable2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer simpleId2;

    private String testVarchar;

    private java.math.BigDecimal testNumeric;

    private java.util.Date testDate;

    private java.time.LocalDateTime testDatetime;

    @OneToMany(mappedBy = "testTable2", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestTable3> testTable3List;

    @ManyToOne
    @JoinColumn(name = "simple_id_1")
    private TestTable1 testTable1;

    public Integer getSimpleId2() {
        return simpleId2;
    }

    public void setSimpleId2(Integer simpleId2) {
        this.simpleId2 = simpleId2;
    }

    public String getTestVarchar() {
        return testVarchar;
    }

    public void setTestVarchar(String testVarchar) {
        this.testVarchar = testVarchar;
    }

    public java.math.BigDecimal getTestNumeric() {
        return testNumeric;
    }

    public void setTestNumeric(java.math.BigDecimal testNumeric) {
        this.testNumeric = testNumeric;
    }

    public java.util.Date getTestDate() {
        return testDate;
    }

    public void setTestDate(java.util.Date testDate) {
        this.testDate = testDate;
    }

    public java.time.LocalDateTime getTestDatetime() {
        return testDatetime;
    }

    public void setTestDatetime(java.time.LocalDateTime testDatetime) {
        this.testDatetime = testDatetime;
    }

    public List<TestTable3> getTestTable3List() {
        return testTable3List;
    }

    public void setTestTable3List(List<TestTable3> testTable3List) {
        this.testTable3List = testTable3List;
    }

    public TestTable1 getTestTable1() {
        return testTable1;
    }

    public void setTestTable1(TestTable1 testTable1) {
        this.testTable1 = testTable1;
    }
}
