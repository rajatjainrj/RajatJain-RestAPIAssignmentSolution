package xyz.rajatjain.employeemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.rajatjain.employeemanagement.models.Employee;

import java.util.List;
import java.util.Optional;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUserName(String username);

    List<Employee> findByFirstName(String firstName);
}
