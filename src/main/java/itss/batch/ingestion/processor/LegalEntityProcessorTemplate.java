package itss.batch.ingestion.processor;

import itss.batch.ingestion.mapper.LegalEntityMapperTemplate;
import itss.batch.ingestion.model.csv.LegalEntityCsvTemplate;
import itss.batch.ingestion.model.request.LegalEntityRequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LegalEntityProcessorTemplate implements ItemProcessor<LegalEntityCsvTemplate, LegalEntityRequestTemplate> {

    private final LegalEntityMapperTemplate legalEntityMapper;

    @Override
    public LegalEntityRequestTemplate process(LegalEntityCsvTemplate legalEntityCsv) throws Exception {
        if (legalEntityCsv.getLegalEntityType().equals("")) {
             throw new NullPointerException();
        }
        return legalEntityMapper.CsvToRequest(legalEntityCsv);
    }
}
