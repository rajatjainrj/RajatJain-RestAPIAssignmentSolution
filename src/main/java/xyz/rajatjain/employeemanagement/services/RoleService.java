package xyz.rajatjain.employeemanagement.services;

import org.springframework.http.ResponseEntity;
import xyz.rajatjain.employeemanagement.dtos.ResponseObj;
import xyz.rajatjain.employeemanagement.dtos.RoleDto;
import xyz.rajatjain.employeemanagement.models.Role;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
public interface RoleService {

    ResponseEntity<ResponseObj> createUpdateRole(RoleDto roleDto);

    ResponseEntity<ResponseObj> getAllRoles();

    Role checkAndCreateRole(String role);

    Role getRoleById(Long id);
}
