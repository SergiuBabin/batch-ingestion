package itss.batch.ingestion.mapper;

import itss.batch.ingestion.model.csv.ContactsCsv;
import itss.batch.ingestion.model.pojos.Accounts;
import itss.batch.ingestion.model.pojos.Contacts;
import itss.batch.ingestion.model.request.ContactsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactsMapper {

    @Mapping(source = "externalUserId", target = "accessContext.externalUserId")
    @Mapping(target = "contacts", expression = "java(addContacts(csv))")
    ContactsRequest CsvToRequest(ContactsCsv csv);

    default List<Contacts> addContacts(ContactsCsv csv) {
        List<Contacts> contacts = new ArrayList<>();
        contacts.add(new Contacts(csv.getName(), csv.getExternalId(), Collections.singletonList(new Accounts(csv.getIban()))));
        return contacts;
    }
}
