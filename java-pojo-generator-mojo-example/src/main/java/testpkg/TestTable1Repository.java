package testpkg;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spring.testpkg.TestTable1;

@Repository
public interface TestTable1Repository extends CrudRepository<TestTable1, Integer> {
    TestTable1 findBySimpleId1(Integer id);
}
