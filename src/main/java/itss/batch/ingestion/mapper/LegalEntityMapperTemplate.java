package itss.batch.ingestion.mapper;

import itss.batch.ingestion.model.csv.LegalEntityCsvTemplate;
import itss.batch.ingestion.model.request.LegalEntityRequestTemplate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LegalEntityMapperTemplate {
    LegalEntityRequestTemplate CsvToRequest(LegalEntityCsvTemplate csv);
}
