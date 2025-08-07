package org.db2code.generator.java.pojo.adapter;

public class RelationAdapter {
    private final String targetClassName;
    private final String fieldName;
    private final String methodName;
    private final String joinColumn;

    public RelationAdapter(String targetClassName, String fieldName, String methodName, String joinColumn) {
        this.targetClassName = targetClassName;
        this.fieldName = fieldName;
        this.methodName = methodName;
        this.joinColumn = joinColumn;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getJoinColumn() {
        return joinColumn;
    }
}
