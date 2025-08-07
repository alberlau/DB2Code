package hibernate.testpkg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTable3Repository extends JpaRepository<TestTable3, Integer> {}
