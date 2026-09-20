package universitySchoolOS.exceptions;

import lombok.Data;

@Data
public class BusinessValidationError extends Exception {

    private final ValidationError validationError;

    public BusinessValidationError(ValidationError validationError) {
        this.validationError = validationError;
    }
}
