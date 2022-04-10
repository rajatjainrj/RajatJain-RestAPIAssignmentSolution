package xyz.rajatjain.employeemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.rajatjain.employeemanagement.models.Role;

import java.util.Optional;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);
}
