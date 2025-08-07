package hibernate.testpkg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTable1Repository extends JpaRepository<TestTable1, Integer> {}
