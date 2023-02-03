package itss.batch.ingestion.model.csv;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemCountAware;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalEntityCsvTemplate implements ItemCountAware {
    private String externalId;
    private String name;
    private String parentExternalId;
    private String legalEntityType;
    private String realmName;
    private int itemCount;
}
