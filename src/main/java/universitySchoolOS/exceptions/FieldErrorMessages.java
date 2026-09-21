package universitySchoolOS.exceptions;

public class FieldErrorMessages {

    public static final String UNAUTHORIZE_ACCESS = "you don't have permission to access this resource";

    /*MANDATORY FIELDS*/
    public static final String USERNAME_NOT_BLANK = "please enter your username";
    public static final String PASSWORD_NOT_BLANK = "please enter your password";
    public static final String MANDATORY_FIELD_MISSING ="messing mandatory field";

    /*INVALID FILED*/
    public static final String USERNAME_INVALID = "Username must be a valid email address";
    public static final String PASSWORD_INVALID = "Password must contain uppercase, lowercase, digit, special character and be at least 8 characters";
    public static final String INVALID_FIELD = "invalid username or password";
}
