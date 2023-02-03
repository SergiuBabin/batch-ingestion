package itss.batch.ingestion.exceptions;

import itss.batch.ingestion.model.request.ContactsRequest;
import org.springframework.web.client.RestClientException;

public class ContactsBulkException extends RestClientException {

    private ContactsRequest contactsRequest;

    public ContactsBulkException(String msg, ContactsRequest contactsRequest) {
        super(msg);
        this.contactsRequest = contactsRequest;
    }

    public ContactsBulkException(String msg, Throwable ex, ContactsRequest contactsRequest) {
        super(msg, ex);
    }

    public ContactsRequest getContactsRequest() {
        return contactsRequest;
    }
}
