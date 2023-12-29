package org.db2code.extractors;

import java.sql.DatabaseMetaData;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.db2code.rawmodel.AbstractRawItem;

public abstract class AbstractExtractor<T extends ExtractionParameters> {
    protected void setProperty(Object object, Object mdValue, String propName) {
        try {
            PropertyUtils.setProperty(object, propName, mdValue);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Failed to set property %s to value %s", propName, mdValue), e);
        }
    }

    public abstract List<? extends AbstractRawItem> extract(
            DatabaseMetaData databaseMetaData, T params);
}
