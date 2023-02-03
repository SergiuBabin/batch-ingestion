package itss.batch.ingestion.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobStatusResponse {
    private JobStatus fullIngestionJobStatus;
    private PartialIngestionStatus partialIngestionStatus;
}
