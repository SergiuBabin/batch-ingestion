package itss.batch.ingestion.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialIngestion {
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private String contactsJobId;
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private String transfersJobId;
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private String savingsJobId;
}
