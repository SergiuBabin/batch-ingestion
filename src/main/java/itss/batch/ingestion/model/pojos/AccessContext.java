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
public class AccessContext {

    @NotEmpty(message = "Field is required! Field value is missing.")
    @Size(max = 64, message = "Value is too long! Must be less than 64 characters")
    @Pattern(regexp = "^[a-zA-Z0-9-]*$", message = "Value contains illegal symbols! " +
        "Must be alpha-numeric characters or dashes")
    private String externalUserId;

    //@Value("${modulesConfigs.contacts.scope}") //TODO check and fix dynamic injection
    private String scope = "USER";
}
