package itss.batch.ingestion.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobTriggerRequest {

    @ApiModelProperty(value = "Id of the Legal Entity", required = true)
    private String externalUserId;

    @ApiModelProperty(value = "Id of the Service Agreement", required = true)
    private String serviceAgreementId;

    @ApiModelProperty(value = "The list of jobs to be skipped or not by giving flag")
    private SkipJobs skipJobs;
}
