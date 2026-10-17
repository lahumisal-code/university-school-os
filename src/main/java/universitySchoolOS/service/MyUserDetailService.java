package universitySchoolOS.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;
    private final UserRolePermissionRepo rolePermissionRepo;

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

        return new UserPrinciple(user, rolePermissions);

    }

}
