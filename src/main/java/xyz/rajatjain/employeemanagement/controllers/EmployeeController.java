package xyz.rajatjain.employeemanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.rajatjain.employeemanagement.dtos.EmployeeDto;
import xyz.rajatjain.employeemanagement.dtos.ResponseObj;
import xyz.rajatjain.employeemanagement.dtos.ResponseObjPagination;
import xyz.rajatjain.employeemanagement.services.EmployeeService;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */

@RestController
@RequestMapping("/v1/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObj> createEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.createEmployee(employeeDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObj> updateEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.updateEmployee(employeeDto);
    }

    @DeleteMapping("/delete/by/id/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObj> deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteEmployee(id);
    }

    @GetMapping("/get/all/page")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ResponseObjPagination> getAllEmployeesPagination(@RequestParam(required = false,
            defaultValue = "0") Integer pageNo,
                                                                           @RequestParam(required = false,
                                                                                   defaultValue = "10") Integer pageSize) {
        return employeeService.getAllEmployeesPagination(pageNo, pageSize);
    }

    @GetMapping("/get/by/id/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ResponseObj> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/search/first/name/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ResponseObj> searchByFirstName(@PathVariable String name) {
        return employeeService.searchByFirstName(name);
    }

    @GetMapping("/sort")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ResponseObj> getAllSorted(@RequestParam String order) {
        return employeeService.getAllSorted(order);
    }
}
