package itss.batch.ingestion.model.pojos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Accounts {
    @NotEmpty(message = "Field is required! Field value is missing.")
    @Size(max = 34, message = "Value is too long!  Must be less than 34 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Value contains illegal symbols! " +
        "Must be only alpha-numeric characters")
    private String iban;
}
