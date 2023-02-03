package itss.batch.ingestion.model.request;


import itss.batch.ingestion.model.pojos.AccessContext;
import itss.batch.ingestion.model.pojos.Contacts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsRequest {

    //@Value("${modulesConfigs.contacts.ingestMode}") //TODO check and fix dynamic injection
    private String ingestMode = "UPSERT";

    private AccessContext accessContext;
    private List<Contacts> contacts;
}
