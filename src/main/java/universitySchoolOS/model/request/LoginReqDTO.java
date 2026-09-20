package universitySchoolOS.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import universitySchoolOS.exceptions.FieldErrorMessages;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReqDTO {

    @NotBlank(message = FieldErrorMessages.USERNAME_NOT_BLANK)
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = FieldErrorMessages.USERNAME_NOT_BLANK)
    private String username;

    @NotBlank(message = FieldErrorMessages.PASSWORD_NOT_BLANK)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = FieldErrorMessages.PASSWORD_NOT_BLANK)
    private String password;
}
