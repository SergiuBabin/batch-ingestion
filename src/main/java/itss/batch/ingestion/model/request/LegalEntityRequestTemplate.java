package itss.batch.ingestion.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalEntityRequestTemplate {
    private String externalId;
    private String name;
    private String parentExternalId;
    private String legalEntityType;
    private String realmName;


}
