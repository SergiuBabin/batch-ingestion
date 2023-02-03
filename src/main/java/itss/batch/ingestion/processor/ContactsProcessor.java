package itss.batch.ingestion.processor;

import itss.batch.ingestion.mapper.ContactsMapper;
import itss.batch.ingestion.model.csv.ContactsCsv;
import itss.batch.ingestion.model.request.ContactsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContactsProcessor implements ItemProcessor<ContactsCsv, ContactsRequest> {

    private final ContactsMapper contactManagerMapper;

    @Override
    public ContactsRequest process(ContactsCsv contactsCsv) throws Exception {
        return contactManagerMapper.CsvToRequest(contactsCsv);
    }
}
