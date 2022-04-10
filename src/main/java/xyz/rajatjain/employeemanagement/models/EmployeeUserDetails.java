package xyz.rajatjain.employeemanagement.models;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
@Slf4j
public class EmployeeUserDetails implements UserDetails {

    private final Employee employee;

    public EmployeeUserDetails(Employee employee) {
        this.employee = employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = employee.getRoles();
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        roles.forEach(r -> grantedAuthorities.add(new SimpleGrantedAuthority(r.getName())));
        log.info(grantedAuthorities.toString());
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getUserName();
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
        return employee.isEnabled();
    }
}
