package testpkg;

/** 
    TEST.TEST_SCHEMA.TEST_TABLE_2
*/
public class TestTable2 {

    /** this is table2 id
        SIMPLE_ID_2 INTEGER(32) Nullable=NO
    */
    private Integer simpleId2;

    /** 
        TEST_VARCHAR CHARACTER VARYING(50) Nullable=YES
    */
    private String testVarchar;

    /** 
        TEST_NUMERIC NUMERIC(10) Nullable=YES
    */
    private java.math.BigDecimal testNumeric;

    /** 
        TEST_DATE DATE(10) Nullable=YES
    */
    private java.util.Date testDate;

    /** 
        TEST_DATETIME TIMESTAMP(26) Nullable=YES
    */
    private java.util.Date testDatetime;

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


    public java.util.Date getTestDatetime() {
        return testDatetime;
    }

    public void setTestDatetime(java.util.Date testDatetime) {
        this.testDatetime = testDatetime;
    }

}
