package org.db2code.generator.java.pojo.adapter;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.sql.JDBCType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.db2code.convert.JavaPropertyConverter;
import org.db2code.rawmodel.RawProcedureParameter;

@EqualsAndHashCode(exclude = {"sqlTypeMapper"})
@ToString
@Data
public class ProcedureParameterAdapter {
    private final RawProcedureParameter rawProcedureParameter;
    private final SqlTypeMapper sqlTypeMapper;
    private int position;

    public ProcedureParameterAdapter(
            RawProcedureParameter rawProcedureParameter, DateImpl dateImpl, String typeMapFile) {
        this.rawProcedureParameter = rawProcedureParameter;
        sqlTypeMapper = new SqlTypeMapper(dateImpl, typeMapFile);
    }

    public String getName() {
        return JavaPropertyConverter.camelCaseFromSnakeCaseInitLow(
                rawProcedureParameter.getColumnName());
    }

    public String getType() {
        return sqlTypeMapper.getMappedType(Integer.parseInt(rawProcedureParameter.getDataType()));
    }

    public boolean getIsInput() {
        return "1".equals(rawProcedureParameter.getColumnType())
                || "2".equals(rawProcedureParameter.getColumnType());
    }

    public boolean getIsOutput() {
        return "2".equals(rawProcedureParameter.getColumnType())
                || "3".equals(rawProcedureParameter.getColumnType());
    }

    public boolean getCanBeUsedAsSingleReturn() {
        return getIsOutput() || getIsInputOutput() || getIsReturn() || getIsResult();
    }

    public boolean getIsInputOutput() {
        return "3".equals(rawProcedureParameter.getColumnType());
    }

    public boolean getIsReturn() {
        return "4".equals(rawProcedureParameter.getColumnType());
    }

    public boolean getIsResult() {
        return "5".equals(rawProcedureParameter.getColumnType());
    }

    public RawProcedureParameter getRawParameter() {
        return rawProcedureParameter;
    }

    public Boolean getIsLast() {
        return rawProcedureParameter.getIsLast();
    }

    public String getJdbcType() {
        String dataType = rawProcedureParameter.getDataType();
        if (isEmpty(dataType)) {
            return null;
        }
        return JDBCType.valueOf(Integer.valueOf(dataType)).getName();
    }
}
