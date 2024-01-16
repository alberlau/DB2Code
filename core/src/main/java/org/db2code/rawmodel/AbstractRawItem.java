package org.db2code.rawmodel;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class AbstractRawItem implements Serializable {
    private Boolean isLast;
}
