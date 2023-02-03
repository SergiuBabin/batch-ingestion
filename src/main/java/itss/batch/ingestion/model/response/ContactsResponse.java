package itss.batch.ingestion.model.response;


import itss.batch.ingestion.model.pojos.Additions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsResponse {
    private int successCount;
    private Additions additions;
}
