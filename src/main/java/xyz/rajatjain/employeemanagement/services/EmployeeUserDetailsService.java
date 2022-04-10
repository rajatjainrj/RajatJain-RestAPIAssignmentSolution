package xyz.rajatjain.employeemanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.rajatjain.employeemanagement.models.Employee;
import xyz.rajatjain.employeemanagement.models.EmployeeUserDetails;
import xyz.rajatjain.employeemanagement.repositories.EmployeeRepository;

import java.util.Optional;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
@Service
public class EmployeeUserDetailsService implements UserDetailsService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> userOptional = employeeRepository.findByUserName(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("username not registered");
        }
        return new EmployeeUserDetails(userOptional.get());
    }
}
