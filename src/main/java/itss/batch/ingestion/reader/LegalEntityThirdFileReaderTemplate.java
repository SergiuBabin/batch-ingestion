package itss.batch.ingestion.reader;


import itss.batch.ingestion.model.csv.LegalEntityCsvTemplate;
import itss.batch.ingestion.utils.ReaderUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

@Component
public class LegalEntityThirdFileReaderTemplate extends FlatFileItemReader<LegalEntityCsvTemplate> implements ItemReader<LegalEntityCsvTemplate> {

    public LegalEntityThirdFileReaderTemplate(@Value("${input.file3}") String inputFile) throws FileNotFoundException {
        setResource(new FileSystemResource(ResourceUtils.getFile("classpath:" + inputFile)));
        setLinesToSkip(1);
        setLineMapper(getDefaultLineMapper());
    }

    private DefaultLineMapper<LegalEntityCsvTemplate> getDefaultLineMapper() {
        String[] names = {"EXTERNAL ID", "NAME", "PARENT EXTERNAL ID", "LEGAL ENTITY TYPE", "REALM NAME"};
        return ReaderUtils.createLineMapper(LegalEntityCsvTemplate.class, names);
    }
}
