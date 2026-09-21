package universitySchoolOS.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor    // parametrize constructor
public class MyApiErrorResponse {

    private Integer errorStatus;
    private String errorCode;
    private String errorType;
    private String errorMessage;

}
