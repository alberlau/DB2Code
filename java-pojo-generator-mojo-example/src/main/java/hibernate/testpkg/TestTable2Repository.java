package hibernate.testpkg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTable2Repository extends JpaRepository<TestTable2, Integer> {}
