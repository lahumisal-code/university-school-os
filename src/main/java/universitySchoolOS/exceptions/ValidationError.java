package universitySchoolOS.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ValidationError {

    UNAUTHORIZE_ACCESS(400,"CRAPE0051", FieldErrorMessages.UNAUTHORIZE_ACCESS,ErrorTypes.unauthorized_access),

    /*MANDATORY FIELD ERROR*/
    MANDATORY_FIELD_MISSING(422,"MFM0001",FieldErrorMessages.MANDATORY_FIELD_MISSING,ErrorTypes.field_error),

    /*INVALID FIELDS- INVALID ENTERED FIELD */
    USERNAME_INVALID(422, "IEF101",FieldErrorMessages.USERNAME_INVALID,ErrorTypes.field_error),
    PASSWORD_INVALID(422,"IEF102",FieldErrorMessages.PASSWORD_INVALID,ErrorTypes.field_error),
    INVALID_FIELD(422,"IEF100",FieldErrorMessages.INVALID_FIELD, ErrorTypes.field_error);
    public final Integer errorStatus;
    public final String errorCode;
    public final String errorMessage;
    public final ErrorTypes errorType;

}
