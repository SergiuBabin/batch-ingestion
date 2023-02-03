package itss.batch.ingestion.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {
    private String fullIngestionJobId;
    private PartialIngestion partialIngestion;
}
