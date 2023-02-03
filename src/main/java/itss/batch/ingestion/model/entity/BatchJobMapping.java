package itss.batch.ingestion.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "BATCH_JOB_MAPPING")
public class BatchJobMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOB_MAPPING_ID", nullable = false)
    private Long mappingId;

    @Column(name = "JOB_INSTANCE_ID", nullable = false)
    private Long instanceId;

    @Column(name = "JOB_NAME")
    private String jobName;

    @Column(name = "JOB_MASTER_ID")
    private String jobMasterId;
}
