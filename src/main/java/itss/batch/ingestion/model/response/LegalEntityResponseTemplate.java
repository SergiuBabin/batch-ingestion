package itss.batch.ingestion.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalEntityResponseTemplate {
    private String name;
    private String internalId;
    private String externalId;
    private String realmName;
    private String[] administrators;
}
