package itss.batch.ingestion.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkipJobs {
    private Boolean skipContacts = false;
    private Boolean skipTransfers = false;
    private Boolean skipSavings = false;
}