package itss.batch.ingestion.model.csv;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsCsv {

    @NotBlank(message = "[externalUserId] Field is required! Field value is missing.")
    @Size(max = 64, message = "[externalUserId] Value is too long! Must be less than 64 characters")
    @Pattern(regexp = "^[a-zA-Z0-9-]*$", message = "[externalUserId] Value contains illegal symbols! " +
        "Must be alpha-numeric characters or dashes")
    private String externalUserId;

    @NotBlank(message = "[name] Field is required! Field value is missing.")
    @Size(max = 140, message = "[name] Value is too long! Must be less than 140 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message = "[name] Value contains illegal symbols! " +
        "Must be alpha-numeric characters or underscores")
    private String name;

    @NotBlank(message = "[externalId] Field is required! Field value is missing.")
    @Size(max = 32, message = "[externalId] Value is too long! Must be less than 32 characters")
    @Pattern(regexp = "^[a-zA-Z0-9-]*$", message = "[externalId] Value contains illegal symbols! " +
        "Must be alpha-numeric characters or dashes")
    private String externalId;

    @NotBlank(message = "[iban] Field is required! Field value is missing.")
    @Size(max = 34, message = "[iban] Value is too long!  Must be less than 34 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "[iban] Value contains illegal symbols! " +
        "Must be only alpha-numeric characters")
    private String iban;
}
