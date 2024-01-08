package org.db2code.rawmodel;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class RawDatabaseMetadata {
    private List<RawTable> tables = new ArrayList<>();
    private List<RawProcedure> procedures = new ArrayList<>();
}
