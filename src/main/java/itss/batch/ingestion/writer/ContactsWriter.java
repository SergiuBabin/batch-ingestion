package itss.batch.ingestion.writer;

import itss.batch.ingestion.model.request.ContactsRequest;
import itss.batch.ingestion.service.ContactsService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.stereotype.Component;

@Component
public class ContactsWriter extends ItemWriterAdapter<ContactsRequest> implements ItemWriter<ContactsRequest> {

    public ContactsWriter(ContactsService contactsService) {
        setTargetMethod("createContactsBulk");
        setTargetObject(contactsService);
    }
}