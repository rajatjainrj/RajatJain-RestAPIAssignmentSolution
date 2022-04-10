package xyz.rajatjain.employeemanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeDto {

    private Long id;

    private String userName;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private List<RoleDto> roles;

}
