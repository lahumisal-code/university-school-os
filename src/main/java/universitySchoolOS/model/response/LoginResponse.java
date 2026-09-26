package universitySchoolOS.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String userType;
    private String token;
    private List<Long> allowedPermissions;
}

