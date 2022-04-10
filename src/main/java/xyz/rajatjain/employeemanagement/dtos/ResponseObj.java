package xyz.rajatjain.employeemanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObj {

    private Object data;

    private ResponseStatus status;

    private String message;

}
