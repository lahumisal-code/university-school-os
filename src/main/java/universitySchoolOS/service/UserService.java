package universitySchoolOS.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import universitySchoolOS.model.UserRolePermissions;
import universitySchoolOS.model.Users;
import universitySchoolOS.model.request.LoginReqDTO;
import universitySchoolOS.model.request.PermissionDTO;
import universitySchoolOS.model.request.RegisterUserDTO;
import universitySchoolOS.model.response.LoginResponse;
import universitySchoolOS.repository.UserRepo;
import universitySchoolOS.repository.UserRolePermissionRepo;
import universitySchoolOS.repository.UsosPermissionsRepository;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final UserRolePermissionRepo userRolePermissionRepo;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsosPermissionsRepository usosPermissionsRepository;


    public String registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        Users user = new Users();
//        user.setRole(Roles.STUDENT);
//        user.setUserType(UserType.COLLEGE);
        user.setFirstName(registerUserDTO.getFirstName());
        user.setLastName(registerUserDTO.getLastName());
        user.setEmail(registerUserDTO.getEmail());
        user.setContactNumber(registerUserDTO.getContactNumber());
        user.setPassword(bCryptPasswordEncoder.encode(registerUserDTO.getPassword()));
        userRepo.save(user);
        return "Register success";
    }

    public LoginResponse verifyUser(LoginReqDTO loginReqDTO) {
        log.info("verifying the user");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReqDTO.getUsername(), loginReqDTO.getPassword()));
        if(authentication.isAuthenticated()){

            Users dbUser = getActiveUser(loginReqDTO.getUsername());
            UserRolePermissions rolePermissions = getRolePermissions(dbUser.getUserId());
            List<PermissionDTO> permissions = getPermissionsListAgainstId(rolePermissions);
            String token = jwtService.generateToken(dbUser.getEmail());
            return buildLoginResponse(dbUser, rolePermissions,permissions, token);
        }
        return null; // Return null if authentication fails
    }

    private Users getActiveUser(String email) {
        Users dbUser = userRepo.findByEmail(email);
        if (dbUser == null) {
            log.warn("No user found for email: {}", email);
            throw new RuntimeException("No user found with email: " + email);
        }
        if (!dbUser.isActive()) {
            log.warn("User account not active: {}", email);
            throw new RuntimeException("User account is not active");
        }
        return dbUser;
    }

    private UserRolePermissions getRolePermissions(Long userId) {
        return userRolePermissionRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("No role/permissions configured for user id: " + userId));
    }

    private LoginResponse buildLoginResponse(Users dbUser, UserRolePermissions rolePermissions, List<PermissionDTO> permissions, String token) {

        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setFirstName(dbUser.getFirstName());
        loginResponse.setLastName(dbUser.getLastName());
        loginResponse.setEmail(dbUser.getEmail());
        loginResponse.setAllowedPermissions(permissions);
        loginResponse.setRole(rolePermissions.getRoles());
        loginResponse.setUserType(rolePermissions.getUserType());
        loginResponse.setToken(token);

        return loginResponse;
    }

    private List<PermissionDTO> getPermissionsListAgainstId(UserRolePermissions rolePermissions) {

        List<Long> permissionIds = rolePermissions.getPermissionIdList();

        return usosPermissionsRepository
                .findByPermissionIdIn(permissionIds)
                .stream()
                .map(permission -> new PermissionDTO(
                        permission.getPermissionName(),
                        permission.getPermissionDescription()
                ))
                .toList();
    }
}
