package itss.batch.ingestion.model.csv;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertFalse;

@SpringBootTest
class ContactsCsvTest {

    private Validator validator;

    //TODO To be used for test debugging
    /**if(!violations.isEmpty()) {
        String err= "";
        for (ConstraintViolation<ContactsCsv> violation : violations) {
            err = String.format("%s", violation.getMessage());
            System.out.println(err);
        }
    }*/

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    //TODO PrintTestMessages actual expected

    //Validation on NULL value
    @Test
    void fieldExternalUserIdValueNullShouldFailValidation() {
        //expected: Not Null
        //actual: Null
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setName("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a");
        csvFile.setExternalUserId(null);
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    @Test
    void fieldNameValueNullShouldFailValidation() {
        //expected: Not Null
        //actual: Null
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a");
        csvFile.setName(null);
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    @Test
    void fieldExternalIdValueNullShouldFailValidation() {
        //expected: Not Null
        //actual: Null
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setName("a");
        csvFile.setIban("a");
        csvFile.setExternalId(null);
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    @Test
    void fieldIbanValueNullShouldFailValidation() {
        //expected: Not Null
        //actual: Null
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setName("a");
        csvFile.setExternalId("a");
        csvFile.setIban(null);
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    //Validation on EMPTY value
    @Test
    void fieldExternalUserIdValueEmptyShouldFailValidation() {
        //expected: Not Empty
        //actual: Empty
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setName("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a");
        csvFile.setExternalUserId("");
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    @Test
    void fieldNameValueEmptyShouldFailValidation() {
        //expected: Not Empty
        //actual: Empty
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a");
        csvFile.setName("");
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    @Test
    void fieldExternalIdValueEmptyShouldFailValidation() {
        //expected: Not Empty
        //actual: Empty
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setName("a");
        csvFile.setIban("a");
        csvFile.setExternalId("");
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());

    }

    @Test
    void fieldIbanValueEmptyShouldFailValidation() {
        //expected: Not Empty
        //actual: Empty
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setName("a");
        csvFile.setExternalId("a");
        csvFile.setIban("");
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    //Validation on Size value length
    @Test
    void fieldExternalUserIdValueSizeShouldFailValidation() {
        //expected max: 64 chars
        //actual max: 65 chars
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setName("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a");
        csvFile.setExternalUserId(
            "a8141b9e066211e8ba890ed5f89f7111" +
                "a8141b9e066211e8ba890ed5f89f7111" +
                "X");
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    @Test
    void fieldNameValueSizeShouldFailValidation() {
        //expected max: 140 chars
        //actual max: 141 chars
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a");
        csvFile.setName(
            "a8141b9e066211e8ba890ed5f89f7111" +
                "a8141b9e066211e8ba890ed5f89f7111" +
                "a8141b9e066211e8ba890ed5f89f7111" +
                "a8141b9e066211e8ba890ed5f89f7111" +
                "a8141b9e0662X");
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    @Test
    void fieldExternalIdValueSizeShouldFailValidation() {
        //expected max: 32 chars
        //actual max: 33 chars
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setName("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a");
        csvFile.setExternalId("a8141b9e066211e8ba890ed5f89f7111X");
        Set<ConstraintViolation<ContactsCsv>> violations = this.validator.validate(csvFile);
        assertFalse(violations.isEmpty());
        //assertThat(violations.size()).isEqualTo(1); //TODO explore and fix
    }

    @Test
    void fieldIbanValueSizeShouldFailValidation() {
        //expected max: 34 chars
        //actual max: 35 chars
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setName("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a8141b9e066211e8ba890ed5f89f711111X");
        Set<ConstraintViolation<ContactsCsv>> violations = this.validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    //Validation on Symbols value
    @Test
    void fieldExternalUserIdValueSymbolShouldFailValidation() {
        //expected: A-Z a-z 0-9 -
        //actual: %
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setName("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a");
        csvFile.setExternalUserId("symbol%test");
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    @Test
    void fieldNameValueSymbolShouldFailValidation() {
        //expected: A-Z a-z 0-9 _ emptyspace
        //actual: %
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a");
        csvFile.setName("symbol%test");
        Set<ConstraintViolation<ContactsCsv>> violations = validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    @Test
    void fieldExternalIdValueSymbolShouldFailValidation() {
        //expected: A-Z a-z 0-9 -
        //actual: %
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setName("a");
        csvFile.setExternalId("a");
        csvFile.setIban("a");
        csvFile.setExternalId("symbol%test");
        Set<ConstraintViolation<ContactsCsv>> violations = this.validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

    @Test
    void fieldIbanValueSymbolShouldFailValidation() {
        //expected: A-Z a-z 0-9
        //actual: %
        ContactsCsv csvFile = new ContactsCsv();
        csvFile.setExternalUserId("a");
        csvFile.setName("a");
        csvFile.setExternalId("a");
        csvFile.setIban("symbol%test");
        Set<ConstraintViolation<ContactsCsv>> violations = this.validator.validate(csvFile);
        assertFalse(violations.isEmpty());
    }

}
