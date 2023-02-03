package itss.batch.ingestion.writer;

import itss.batch.ingestion.model.request.LegalEntityRequestTemplate;
import itss.batch.ingestion.service.LegalEntityServiceTemplate;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.stereotype.Component;

@Component
public class LegalEntityWriterTemplate extends ItemWriterAdapter<LegalEntityRequestTemplate> implements ItemWriter<LegalEntityRequestTemplate> {

    public LegalEntityWriterTemplate(LegalEntityServiceTemplate legalEntityService) {
        setTargetMethod("restCallToCreateStudent");
        setTargetObject(legalEntityService);
    }
}