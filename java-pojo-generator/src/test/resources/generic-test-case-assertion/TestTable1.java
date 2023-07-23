package testpkg;

/** test_table_1 description
    TEST.TEST_SCHEMA.TEST_TABLE_1
*/
public class TestTable1 {

    /** 
        SIMPLE_ID_1 INTEGER(32) Nullable=NO
    */
    private Integer simpleId1;

    /** 
        TEST_VARCHAR CHARACTER VARYING(50) Nullable=NO
    */
    private String testVarchar;

    /** 
        TEST_NUMERIC NUMERIC(10) Nullable=NO
    */
    private java.math.BigDecimal testNumeric;

    /** 
        TEST_DATE DATE(10) Nullable=NO
    */
    private java.util.Date testDate;

    /** 
        TEST_DATETIME TIMESTAMP(26) Nullable=NO
    */
    private java.util.Date testDatetime;

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


    public java.util.Date getTestDatetime() {
        return testDatetime;
    }

    public void setTestDatetime(java.util.Date testDatetime) {
        this.testDatetime = testDatetime;
    }

}
