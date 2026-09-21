package universitySchoolOS.exceptions;

import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<String, ValidationError> CONSTANTS_ERROR_MAP = new ConcurrentHashMap<>();

    public GlobalExceptionHandler() {
        CONSTANTS_ERROR_MAP.put(NotNull.class.getSimpleName(), ValidationError.MANDATORY_FIELD_MISSING);
        CONSTANTS_ERROR_MAP.put(NotBlank.class.getSimpleName(), ValidationError.MANDATORY_FIELD_MISSING);
        CONSTANTS_ERROR_MAP.put(NotEmpty.class.getSimpleName(), ValidationError.MANDATORY_FIELD_MISSING);
        CONSTANTS_ERROR_MAP.put(Pattern.class.getSimpleName(), ValidationError.INVALID_FIELD);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);

        ValidationError validationError = CONSTANTS_ERROR_MAP.get(fieldError.getCode());

        MyApiErrorResponse response = new MyApiErrorResponse(
                validationError.getErrorStatus(),
                validationError.getErrorCode(),
                validationError.getErrorType().toString(),
                fieldError.getDefaultMessage()
        );

        return ResponseEntity.status(validationError.getErrorStatus()).body(response);
    }



}
