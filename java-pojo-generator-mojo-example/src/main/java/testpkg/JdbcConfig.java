package testpkg;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.H2Dialect;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {
    @Override
    public Dialect jdbcDialect(NamedParameterJdbcOperations operations) {
        return H2Dialect.INSTANCE;
    }
}
