package org.db2code.generator.java.pojo.adapter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.ToString;
import org.apache.commons.lang3.SerializationUtils;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.rawmodel.RawProcedure;

@ToString(callSuper = true)
public class JavaProcedureAdapter implements ClassAdapter {

    private final RawProcedure rawProcedure;
    private final String targetPackage;
    private final DateImpl dateImpl;
    private final boolean includeGenerationInfo;

    public JavaProcedureAdapter(
            RawProcedure rawProcedure,
            String targetPackage,
            DateImpl dateImpl,
            boolean includeGenerationInfo) {
        this.rawProcedure = rawProcedure;
        this.targetPackage = targetPackage;
        this.dateImpl = dateImpl;
        this.includeGenerationInfo = includeGenerationInfo;
    }

    @Override
    public String getClassName() {
        return JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(rawProcedure.getProcedureName());
    }

    @Override
    public String getPackage() {
        return targetPackage;
    }

    public RawProcedure getRawProcedure() {
        return rawProcedure;
    }

    public Collection<JavaProcedureParameterAdapter> getParameters() {
        return rawProcedure.getParameters().stream()
                .map(
                        rawProcedureParameter ->
                                new JavaProcedureParameterAdapter(rawProcedureParameter, dateImpl))
                .collect(Collectors.toList());
    }

    @Override
    public String getGenerationInfo() {
        if (includeGenerationInfo) {
            return ClassAdapter.super.getGenerationInfo();
        } else {
            return null;
        }
    }

    public JavaProcedureParameterAdapter getSingleParameterReturn() {
        List<JavaProcedureParameterAdapter> params =
                getParameters().stream()
                        .filter(JavaProcedureParameterAdapter::getCanBeUsedAsSingleReturn)
                        .collect(Collectors.toList());

        if (params.size() == 1) { // NOPMD
            return params.get(0);
        }
        return null;
    }

    public Collection<JavaProcedureParameterAdapter> getInputParameters() {
        JavaProcedureParameterAdapter singleParameterReturn = getSingleParameterReturn();
        List<JavaProcedureParameterAdapter> params =
                getParameters().stream()
                        .filter(
                                javaProcedureParameterAdapter ->
                                        !javaProcedureParameterAdapter.equals(
                                                singleParameterReturn))
                        .map(this::cloneParam)
                        .collect(Collectors.toList());
        updateIsLastOnClonedCollection(params);
        return params;
    }

    private static void updateIsLastOnClonedCollection(List<JavaProcedureParameterAdapter> params) {
        JavaProcedureParameterAdapter lastParam = null;
        for (int i = 0; i < params.size(); i++) {
            JavaProcedureParameterAdapter param = params.get(i);
            lastParam = param;
            param.getRawParameter().setIsLast(null);
            param.setPosition(i + 2);
        }
        if (lastParam != null) {
            lastParam.getRawParameter().setIsLast(true);
        }
    }

    private JavaProcedureParameterAdapter cloneParam(
            JavaProcedureParameterAdapter javaProcedureParameterAdapter) {
        return new JavaProcedureParameterAdapter(
                SerializationUtils.clone(javaProcedureParameterAdapter.getRawParameter()),
                dateImpl);
    }
}
