package xyz.rajatjain.employeemanagement.services;

import org.springframework.http.ResponseEntity;
import xyz.rajatjain.employeemanagement.dtos.EmployeeDto;
import xyz.rajatjain.employeemanagement.dtos.ResponseObj;
import xyz.rajatjain.employeemanagement.dtos.ResponseObjPagination;
import xyz.rajatjain.employeemanagement.models.Role;

import java.util.Set;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
public interface EmployeeService {
    void createEmployee(Set<Role> roles);

    void createEmployee(String email, String firstName, String lastName, String username, Set<Role> roles);

    ResponseEntity<ResponseObj> createEmployee(EmployeeDto employeeDto);

    ResponseEntity<ResponseObjPagination> getAllEmployeesPagination(Integer pageNo, Integer pageSize);

    ResponseEntity<ResponseObj> getEmployeeById(Long id);

    ResponseEntity<ResponseObj> deleteEmployee(Long id);

    ResponseEntity<ResponseObj> searchByFirstName(String name);

    ResponseEntity<ResponseObj> getAllSorted(String order);

    ResponseEntity<ResponseObj> updateEmployee(EmployeeDto employeeDto);
}
