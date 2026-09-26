package universitySchoolOS.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usos_role_permissions")
@Data
public class UserRolePermissions {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;

    @Column(name = "roles", nullable = false, length = 50)
    private String roles;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "permission_id_list", nullable = false)
    private List<Long> permissionIdList = new ArrayList<>();

}