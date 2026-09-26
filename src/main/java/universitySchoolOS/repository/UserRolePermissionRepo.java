package universitySchoolOS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import universitySchoolOS.model.UserRolePermissions;

import java.util.List;

@Repository
public interface UserRolePermissionRepo extends JpaRepository<UserRolePermissions, Long> {

//    List<String> findPermissionNamesByIds(List<Long> permissionIds);
}
