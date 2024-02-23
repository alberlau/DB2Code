package org.db2code.extractors;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.db2code.md.ResultsetMetadata;
import org.db2code.rawmodel.AbstractRawItem;

@Slf4j
public abstract class AbstractExtractor<T extends ExtractionParameters> {
    protected void setProperty(Object object, Object mdValue, String propName) {
        if (mdValue instanceof Number) {
            mdValue = ((Number) mdValue).intValue();
        }
        try {
            PropertyUtils.setProperty(object, propName, mdValue);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Failed to set property %s to value %s", propName, mdValue), e);
        }
    }

    protected String tryGetFromMetadata(ResultsetMetadata mdItem, ResultSet metadataRs) {
        try {
            return metadataRs.getString(mdItem.getName());
        } catch (SQLException e) {
            log.warn(
                    "Problem while getting metadata: "
                            + e.getMessage()
                            + " it will be not set in model.");
        }
        return null;
    }

    public abstract List<? extends AbstractRawItem> extract(
            DatabaseMetaData databaseMetaData, T params);
}
