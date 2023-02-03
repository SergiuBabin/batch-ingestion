package itss.batch.ingestion.processor;

import itss.batch.ingestion.mapper.LegalEntityMapperTemplate;
import itss.batch.ingestion.model.csv.LegalEntityCsvTemplate;
import itss.batch.ingestion.model.request.LegalEntityRequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LegalEntityProcessor implements ItemProcessor<LegalEntityCsvTemplate, LegalEntityRequestTemplate> {

    private final LegalEntityMapperTemplate legalEntityMapper;

    @Override
    public LegalEntityRequestTemplate process(LegalEntityCsvTemplate legalEntityCsv) {
        if (legalEntityCsv.getLegalEntityType().equals("")) {
            throw new FlatFileParseException("Legal Entity is missing!", legalEntityCsv.toString());
        }
        return legalEntityMapper.CsvToRequest(legalEntityCsv);
    }
}
