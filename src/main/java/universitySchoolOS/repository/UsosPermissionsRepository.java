package universitySchoolOS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import universitySchoolOS.model.UsosPermissions;

import java.util.List;

@Repository
public interface UsosPermissionsRepository
        extends JpaRepository<UsosPermissions, Long> {

    List<UsosPermissions> findByPermissionIdIn(List<Long> permissionIds);
}