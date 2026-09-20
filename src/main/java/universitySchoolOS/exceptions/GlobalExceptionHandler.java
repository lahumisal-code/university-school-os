package universitySchoolOS.exceptions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<String, ValidationError> CONSTANTS_ERROR_MAP = new ConcurrentHashMap<>();

    public GlobalExceptionHandler() {
        CONSTANTS_ERROR_MAP.put(NotNull.class.getSimpleName(), ValidationError.MANDATORY_FIELD_MISSING);
        CONSTANTS_ERROR_MAP.put(NotBlank.class.getSimpleName(), ValidationError.MANDATORY_FIELD_MISSING);
        CONSTANTS_ERROR_MAP.put(NotEmpty.class.getSimpleName(), ValidationError.MANDATORY_FIELD_MISSING);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> MethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        fieldErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ValidationError validationError = ValidationError.MANDATORY_FIELD_MISSING;
 
        MyApiErrorResponse response = new MyApiErrorResponse(
                validationError.getErrorStatus(),
                validationError.getErrorCode(),
                validationError.getErrorType().toString(),
                validationError.getErrorMessage()
        );

        return ResponseEntity
                .status(validationError.getErrorStatus())
                .body(response);
    }



}
