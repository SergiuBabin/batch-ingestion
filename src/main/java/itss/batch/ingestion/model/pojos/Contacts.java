package itss.batch.ingestion.model.pojos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contacts {

    @NotEmpty(message = "Field is required! Field value is missing.")
    @Size(max = 140, message = "Value is too long! Must be less than 140 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Value contains illegal symbols! " +
        "Must be alpha-numeric characters or underscores")
    private String name;
    @NotEmpty(message = "Field is required! Field value is missing.")
    @Size(max = 32, message = "Value is too long! Must be less than 32 characters")
    @Pattern(regexp = "^[a-zA-Z0-9-]*$", message = "Value contains illegal symbols! " +
        "Must be alpha-numeric characters or dashes")
    private String externalId;
    private List<Accounts> accounts;
}
