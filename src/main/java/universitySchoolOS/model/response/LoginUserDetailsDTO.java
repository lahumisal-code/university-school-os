package universitySchoolOS.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import universitySchoolOS.model.UserRolePermissions;
import universitySchoolOS.model.Users;
import universitySchoolOS.model.request.PermissionDTO;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDetailsDTO {

    private Users user;
    private UserRolePermissions rolePermissions;
    private List<PermissionDTO> permissions;
}
