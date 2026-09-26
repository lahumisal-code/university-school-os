package universitySchoolOS.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import universitySchoolOS.model.enums.Roles;
import universitySchoolOS.model.enums.UserType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrinciple implements UserDetails {

    private Users users;
    private final UserRolePermissions rolePermissions;


    public UserPrinciple(Users users, UserRolePermissions rolePermissions) {
        this.users = users;
        this.rolePermissions = rolePermissions;
    }

    public Users getUsers() {
        return users;
    }

    public UserRolePermissions getRolePermissions() {
        return rolePermissions;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(rolePermissions.getRoles()));
        authorities.add(new SimpleGrantedAuthority(rolePermissions.getUserType()));
        authorities.add(new SimpleGrantedAuthority(rolePermissions.getPermissionIdList().toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
