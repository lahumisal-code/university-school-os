package universitySchoolOS.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import universitySchoolOS.model.UserPrinciple;
import universitySchoolOS.model.UserRolePermissions;
import universitySchoolOS.model.Users;
import universitySchoolOS.repository.UserRepo;
import universitySchoolOS.repository.UserRolePermissionRepo;
//import universitySchoolOS.repository.UserRolePermissionRepo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;
    private final UserRolePermissionRepo rolePermissionRepo;
    public MyUserDetailService(UserRepo repo, UserRolePermissionRepo  rolePermissionRepo) {
        this.userRepo = repo;
        this.rolePermissionRepo = rolePermissionRepo;
    }

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("Username not found");
        }

        UserRolePermissions rolePermissions = rolePermissionRepo.findById(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No role/permissions configured for user: " + username));

//        List<String> permissionNames = resolvePermissionNames(rolePermissions.getPermissionIdList());

        return new UserPrinciple(user, rolePermissions);

    }

//    private List<String> resolvePermissionNames(List<Long> permissionIds) {
//        if (permissionIds == null || permissionIds.isEmpty()) {
//            return Collections.emptyList();
//        }
//        return rolePermissionRepo.findPermissionNamesByIds(permissionIds);
//    }

}
