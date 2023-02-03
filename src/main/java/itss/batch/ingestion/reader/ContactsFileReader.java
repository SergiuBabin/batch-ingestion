package itss.batch.ingestion.reader;


import itss.batch.ingestion.model.csv.ContactsCsv;
import itss.batch.ingestion.utils.ReaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
public class ContactsFileReader extends FlatFileItemReader<ContactsCsv> {

    public ContactsFileReader(@Value("${input.file4}") String inputFile, @Value("${lines.skip.contactsReader}") Integer linesToSkip) throws FileNotFoundException {
        setResource(new FileSystemResource(ResourceUtils.getFile("classpath:" + inputFile)));
        setLinesToSkip(linesToSkip);
        setLineMapper(getDefaultLineMapper());
    }

    private DefaultLineMapper<ContactsCsv> getDefaultLineMapper() {
        String[] names = {"EXTERNAL USER ID", "NAME", "EXTERNAL ID", "IBAN"};
        return ReaderUtils.createLineMapper(ContactsCsv.class, names);
    }

    private Validator factory = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public ContactsCsv doRead() throws Exception {
        ContactsCsv contactsCsv = super.doRead();

        if (Objects.isNull(contactsCsv)) return null;

        Set<ConstraintViolation<ContactsCsv>> violations = this.factory.validate(contactsCsv);
        if (!violations.isEmpty()) {
            String errorMsg = "";
            for (ConstraintViolation<ContactsCsv> violation : violations) {
                errorMsg = String.format("%s", violation.getMessage());
            }
            throw new FlatFileParseException(errorMsg, Objects.toString(contactsCsv));
        }
        else {
            return contactsCsv;
        }
    }






}
