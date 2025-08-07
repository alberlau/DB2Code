package org.db2code.generator.java.pojo.adapter;

public class ManyToManyRelationAdapter extends RelationAdapter {
    private final String joinTable;
    private final String inverseJoinColumn;

    public ManyToManyRelationAdapter(
            String targetClassName,
            String fieldName,
            String methodName,
            String joinColumn,
            String joinTable,
            String inverseJoinColumn) {
        super(targetClassName, fieldName, methodName, joinColumn);
        this.joinTable = joinTable;
        this.inverseJoinColumn = inverseJoinColumn;
    }

    public String getJoinTable() {
        return joinTable;
    }

    public String getInverseJoinColumn() {
        return inverseJoinColumn;
    }
}
