package itss.batch.ingestion.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobStatusRequest {
    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Full Ingestion Job Id may be in UUID format")
    private String fullIngestionJobId;

    @Pattern(regexp = "^\\d*$", message = "Contacts Job Id may be a number")
    private String contactsJobId;

    @Pattern(regexp = "^\\d*$", message = "Current Account Job Id may be a number")
    private String transfersJobId;

    @Pattern(regexp = "^\\d*$", message = "Savings Job Id may be a number")
    private String savingsJobId;
}
