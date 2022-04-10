package xyz.rajatjain.employeemanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeListDto {

    private Long id;

    private String userName;

    private String email;

    private String firstName;

    private String lastName;


}
