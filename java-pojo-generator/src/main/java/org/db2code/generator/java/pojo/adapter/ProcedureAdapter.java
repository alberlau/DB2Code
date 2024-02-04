package org.db2code.generator.java.pojo.adapter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.ToString;
import org.apache.commons.lang3.SerializationUtils;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.generator.java.pojo.ExecutorParams;
import org.db2code.rawmodel.RawProcedure;

@ToString(callSuper = true)
public class ProcedureAdapter implements ClassAdapter {

    private final RawProcedure rawProcedure;
    private final ExecutorParams params;

    public ProcedureAdapter(RawProcedure rawProcedure, ExecutorParams params) {
        this.rawProcedure = rawProcedure;
        this.params = params;
    }

    @Override
    public String getClassName() {
        return JavaPropertyConverter.camelCaseFromSnakeCaseInitCap(rawProcedure.getProcedureName());
    }

    @Override
    public String getPackage() {
        return params.getGeneratorTarget().getTargetPackage();
    }

    public RawProcedure getRawProcedure() {
        return rawProcedure;
    }

    public Collection<ProcedureParameterAdapter> getParameters() {
        return rawProcedure.getParameters().stream()
                .map(
                        rawProcedureParameter ->
                                new ProcedureParameterAdapter(
                                        rawProcedureParameter,
                                        params.getDateImpl(),
                                        params.getTypeMapFile()))
                .collect(Collectors.toList());
    }

    @Override
    public String getGenerationInfo() {
        if (params.isIncludeGenerationInfo()) {
            return ClassAdapter.super.getGenerationInfo();
        } else {
            return null;
        }
    }

    public ProcedureParameterAdapter getSingleParameterReturn() {
        List<ProcedureParameterAdapter> params =
                getParameters().stream()
                        .filter(ProcedureParameterAdapter::getCanBeUsedAsSingleReturn)
                        .collect(Collectors.toList());

        if (params.size() == 1) { // NOPMD
            return params.get(0);
        }
        return null;
    }

    public Collection<ProcedureParameterAdapter> getInputParameters() {
        ProcedureParameterAdapter singleParameterReturn = getSingleParameterReturn();
        List<ProcedureParameterAdapter> params =
                getParameters().stream()
                        .filter(
                                procedureParameterAdapter ->
                                        !procedureParameterAdapter.equals(singleParameterReturn))
                        .map(this::cloneParam)
                        .collect(Collectors.toList());
        updateIsLastOnClonedCollection(params);
        return params;
    }

    private static void updateIsLastOnClonedCollection(List<ProcedureParameterAdapter> params) {
        ProcedureParameterAdapter lastParam = null;
        for (int i = 0; i < params.size(); i++) {
            ProcedureParameterAdapter param = params.get(i);
            lastParam = param;
            param.getRawParameter().setIsLast(null);
            param.setPosition(i + 2);
        }
        if (lastParam != null) {
            lastParam.getRawParameter().setIsLast(true);
        }
    }

    private ProcedureParameterAdapter cloneParam(
            ProcedureParameterAdapter procedureParameterAdapter) {
        return new ProcedureParameterAdapter(
                SerializationUtils.clone(procedureParameterAdapter.getRawParameter()),
                params.getDateImpl(),
                params.getTypeMapFile());
    }
}
