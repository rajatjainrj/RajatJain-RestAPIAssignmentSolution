package xyz.rajatjain.employeemanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.rajatjain.employeemanagement.models.Role;
import xyz.rajatjain.employeemanagement.utils.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
@Service
public class PostStartupScriptImpl implements PostStartupScript {

    private RoleService roleService;

    private EmployeeService employeeService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void initializeRolesAndDefaultEmployees() {
        Role adminRole = roleService.checkAndCreateRole("ADMIN");
        Role employeeRole = roleService.checkAndCreateRole("EMPLOYEE");

        initiateUsers(adminRole, employeeRole);

    }

    public void initiateUsers(Role adminRole, Role employeeRole) {
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminRoles.add(employeeRole);

        Set<Role> employeeRoles = new HashSet<>();
        employeeRoles.add(employeeRole);

        // Create 1 default Admin
        employeeService.createEmployee(
                "contact@rajatjain.xyz",
                "Rajat",
                "Jain",
                "rajatjain",
                adminRoles);

        // Create 1 default Employee
        employeeService.createEmployee(
                "second@rajatjain.xyz",
                "Rajat",
                "Solanki",
                "rajatsolanki",
                employeeRoles);

        for (int i = 1; i < Constants.NO_OF_DEFAULT_ADMIN_EMPLOYEES; i++) {
            employeeService.createEmployee(adminRoles);
        }

        for (int i = 1; i < Constants.NO_OF_DEFAULT_EMPLOYEES; i++) {
            employeeService.createEmployee(employeeRoles);
        }
    }


}
