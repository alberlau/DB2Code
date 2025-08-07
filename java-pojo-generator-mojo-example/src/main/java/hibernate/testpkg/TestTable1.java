package hibernate.testpkg;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "TEST_TABLE_1")
public class TestTable1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer simpleId1;

    @OneToMany(mappedBy = "testTable1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestTable3> testTable3List;

    private String testVarchar;

    private java.math.BigDecimal testNumeric;

    private java.util.Date testDate;

    private Boolean someBool;

    private java.time.LocalDateTime testDatetime;

    public List<TestTable3> getTestTable3List() {
        return testTable3List;
    }

    public void setTestTable3List(List<TestTable3> testTable3List) {
        this.testTable3List = testTable3List;
    }

    public Integer getSimpleId1() {
        return simpleId1;
    }

    public void setSimpleId1(Integer simpleId1) {
        this.simpleId1 = simpleId1;
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

    public Boolean getSomeBool() {
        return someBool;
    }

    public void setSomeBool(Boolean someBool) {
        this.someBool = someBool;
    }

    public java.time.LocalDateTime getTestDatetime() {
        return testDatetime;
    }

    public void setTestDatetime(java.time.LocalDateTime testDatetime) {
        this.testDatetime = testDatetime;
    }
}
