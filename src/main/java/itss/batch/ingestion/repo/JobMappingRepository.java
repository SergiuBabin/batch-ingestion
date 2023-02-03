package itss.batch.ingestion.repo;

import itss.batch.ingestion.model.entity.BatchJobMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobMappingRepository extends JpaRepository<BatchJobMapping, Long> {

    List<BatchJobMapping> findByJobMasterId(String jobMasterId);
}
