package itss.batch.ingestion.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialIngestionStatus {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JobStatus contactsJobStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JobStatus transfersJobStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JobStatus savingsJobStatus;
}
