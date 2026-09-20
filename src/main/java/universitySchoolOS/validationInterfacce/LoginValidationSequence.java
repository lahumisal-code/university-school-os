package universitySchoolOS.validationInterfacce;

import jakarta.validation.GroupSequence;

@GroupSequence({
        MandatoryValidation.class,
        FormatValidation.class
})
public interface LoginValidationSequence {
}