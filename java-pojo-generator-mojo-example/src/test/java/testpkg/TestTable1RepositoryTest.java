package testpkg;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import spring.testpkg.TestTable1;

@SpringBootTest(classes = {App.class})
public class TestTable1RepositoryTest {

    private final TestTable1Repository repository;

    public TestTable1RepositoryTest(@Autowired TestTable1Repository repository) {
        this.repository = repository;
    }

    @Test
    public void testSaveAndFind() {
        TestTable1 entity = new TestTable1();
        entity.setTestVarchar("Test Name");
        entity.setTestNumeric(BigDecimal.ONE);
        entity.setTestDate(LocalDate.now());
        entity.setTestDatetime(LocalDate.now().atStartOfDay());
        TestTable1 savedEntity = repository.save(entity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getSimpleId1()).isNotNull();

        assertThat(savedEntity.getTestVarchar()).isEqualTo(entity.getTestVarchar());

        TestTable1 foundEntity = repository.findById(savedEntity.getSimpleId1()).orElse(null);
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getTestVarchar()).isEqualTo("Test Name");
    }

    @Test
    public void testSaveWithConstraintViolation() {
        TestTable1 entity = new TestTable1();
        assertThrows(
                DbActionExecutionException.class,
                () -> {
                    repository.save(entity);
                });
    }
}
