package xyz.rajatjain.employeemanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.rajatjain.employeemanagement.dtos.ResponseObj;
import xyz.rajatjain.employeemanagement.dtos.RoleDto;
import xyz.rajatjain.employeemanagement.services.RoleService;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/v1/role")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create/update")
    public ResponseEntity<ResponseObj> createUpdateRole(@RequestBody RoleDto roleDto) {
        return roleService.createUpdateRole(roleDto);
    }

    @GetMapping("/get/all")
    public ResponseEntity<ResponseObj> getAllRoles() {
        return roleService.getAllRoles();
    }

}
