package xyz.rajatjain.employeemanagement.services;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.rajatjain.employeemanagement.dtos.EmployeeDto;
import xyz.rajatjain.employeemanagement.dtos.EmployeeListDto;
import xyz.rajatjain.employeemanagement.dtos.ResponseObj;
import xyz.rajatjain.employeemanagement.dtos.ResponseObjPagination;
import xyz.rajatjain.employeemanagement.dtos.RoleDto;
import xyz.rajatjain.employeemanagement.models.Employee;
import xyz.rajatjain.employeemanagement.models.Role;
import xyz.rajatjain.employeemanagement.repositories.EmployeeRepository;
import xyz.rajatjain.employeemanagement.utils.CommonUtils;
import xyz.rajatjain.employeemanagement.utils.Constants;
import xyz.rajatjain.employeemanagement.utils.ResponseUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private PasswordEncoder passwordEncoder;

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public void createEmployee(Set<Role> roles) {

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-US"), new RandomService());
        Faker faker = new Faker();

        String email = fakeValuesService.bothify("????##@gmail.com");
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String username = (firstName + lastName).toLowerCase(Locale.ROOT);

        createEmployee(email, firstName, lastName, username, roles);

    }

    @Override
    public void createEmployee(String email, String firstName, String lastName, String username, Set<Role> roles) {
        Optional<Employee> employeeCheck = employeeRepository.findByUserName(username);
        if (employeeCheck.isEmpty()) {
            Employee employee =
                    Employee.builder()
                            .userName(username)
                            .password(passwordEncoder.encode(username))
                            .email(email)
                            .firstName(firstName)
                            .lastName(lastName)
                            .roles(roles)
                            .enabled(true)
                            .creationDate(CommonUtils.getCurrentTimestamp())
                            .updateDate(CommonUtils.getCurrentTimestamp())
                            .build();
            employeeRepository.save(employee);
        }
    }

    @Override
    public ResponseEntity<ResponseObj> createEmployee(EmployeeDto employeeDto) {
        try {
            if (employeeDto.getId() != null) {
                employeeDto.setId(null);
            }

            if (employeeDto.getRoles() == null || employeeDto.getRoles().isEmpty()) {
                return ResponseUtil.getFailureResponse("Please provide roles for the employee");
            }

            Optional<Employee> employeeByUserNameOptional =
                    employeeRepository.findByUserName(employeeDto.getUserName().trim());
            if (employeeByUserNameOptional.isPresent()) {
                return ResponseUtil.getFailureResponse("A employee with the same user name already exists");
            }

            Employee employee = new Employee();
            employee.setCreationDate(CommonUtils.getCurrentTimestamp());
            employee.setUpdateDate(CommonUtils.getCurrentTimestamp());
            BeanUtils.copyProperties(employeeDto, employee, CommonUtils.getNullPropertyNames(employeeDto));

            try {
                Set<Role> roleList = getRoles(employeeDto.getRoles());
                if (!roleList.isEmpty()) {
                    employee.setRoles(roleList);
                }
            } catch (NoSuchElementException e) {
                return ResponseUtil.getFailureResponse("Invalid Role ID");
            }

            employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));

            employee = employeeRepository.save(employee);

            return ResponseUtil.getSuccessResponse(convertEmployeeToEmployeeDto(employee),
                    Constants.DATA_SAVE_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("Error in createEmployee", e);
            return ResponseUtil.getErrorResponse(e);
        }
    }

    @Override
    public ResponseEntity<ResponseObj> updateEmployee(EmployeeDto employeeDto) {
        try {
            Employee employee;
            Optional<Employee> employeeOptional = employeeRepository.findById(employeeDto.getId());
            if (employeeOptional.isEmpty()) {
                return ResponseUtil.getFailureResponse("Employee not found with ID");
            }
            employee = employeeOptional.get();
            employeeDto.setPassword(null);

            Optional<Employee> employeeByUserNameOptional =
                    employeeRepository.findByUserName(employeeDto.getUserName().trim());
            if (employeeByUserNameOptional.isPresent() && employeeByUserNameOptional.get().getId().longValue() != employeeDto.getId().longValue()) {
                return ResponseUtil.getFailureResponse("A employee with the same user name already exists");
            }

            employee.setUpdateDate(CommonUtils.getCurrentTimestamp());
            BeanUtils.copyProperties(employeeDto, employee, CommonUtils.getNullPropertyNames(employeeDto));

            try {
                Set<Role> roleList = getRoles(employeeDto.getRoles());
                if (!roleList.isEmpty()) {
                    employee.setRoles(roleList);
                }
            } catch (NoSuchElementException e) {
                return ResponseUtil.getFailureResponse("Invalid Role ID");
            }

            employee = employeeRepository.save(employee);

            return ResponseUtil.getSuccessResponse(convertEmployeeToEmployeeListDto(employee),
                    Constants.DATA_SAVE_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("Error in updateEmployee", e);
            return ResponseUtil.getErrorResponse(e);
        }
    }

    private Set<Role> getRoles(List<RoleDto> roles) {
        Set<Role> roleList = new HashSet<>();
        if (roles != null && !roles.isEmpty()) {
            roles.forEach((r) -> roleList.add(roleService.getRoleById(r.getId())));
        }
        return roleList;
    }

    @Override
    public ResponseEntity<ResponseObjPagination> getAllEmployeesPagination(Integer pageNo, Integer pageSize) {
        try {
            Page<Employee> employeePage = employeeRepository.findAll(PageRequest.of(pageNo, pageSize));
            return ResponseUtil.getSuccessResponsePagination(convertEmployeeListToEmployeeListDto(employeePage.getContent()), Constants.GET_SUCCESS_MSG,
                    employeePage.getTotalPages(), employeePage.getTotalElements());
        } catch (Exception e) {
            log.error("Error in getAllEmployeesPagination", e);
            return ResponseUtil.getErrorResponsePagination(e);
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getEmployeeById(Long id) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(id);
            if (employeeOptional.isEmpty()) {
                return ResponseUtil.getFailureResponse("Employee with this id not found");
            }
            return ResponseUtil.getSuccessResponse(convertEmployeeToEmployeeListDto(employeeOptional.get()),
                    Constants.GET_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("Error in getEmployeeById", e);
            return ResponseUtil.getErrorResponse(e);
        }
    }

    @Override
    public ResponseEntity<ResponseObj> deleteEmployee(Long id) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(id);
            if (employeeOptional.isEmpty()) {
                return ResponseUtil.getFailureResponse("Employee with this id not found");
            }
            employeeRepository.deleteById(id);

            return ResponseUtil.getSuccessResponse(null, "Employee with ID - " + id + " Deleted successfully");
        } catch (Exception e) {
            log.error("Error in deleteEmployee", e);
            return ResponseUtil.getErrorResponse(e);
        }
    }

    @Override
    public ResponseEntity<ResponseObj> searchByFirstName(String name) {
        try {
            List<Employee> employeeList = employeeRepository.findByFirstName(name);
            return ResponseUtil.getSuccessResponse(convertEmployeeListToEmployeeListDto(employeeList),
                    Constants.GET_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("Error in searchByFirstName", e);
            return ResponseUtil.getErrorResponse(e);
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllSorted(String order) {
        try {
            Sort.Direction direction = Sort.Direction.DESC;
            if (order.equals("asc")) {
                direction = Sort.Direction.ASC;
            }
            List<Employee> employeeList = employeeRepository.findAll(Sort.by(direction, "firstName"));
            return ResponseUtil.getSuccessResponse(convertEmployeeListToEmployeeListDto(employeeList),
                    Constants.GET_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("Error in searchByFirstName", e);
            return ResponseUtil.getErrorResponse(e);
        }
    }

    private EmployeeListDto convertEmployeeToEmployeeListDto(Employee employee) {
        EmployeeListDto employeeListDto = new EmployeeListDto();
        BeanUtils.copyProperties(employee, employeeListDto);
        return employeeListDto;
    }

    private EmployeeDto convertEmployeeToEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        BeanUtils.copyProperties(employee, employeeDto);
        employeeDto.setRoles(convertRolesListToRolesDtoList(employee.getRoles()));
        return employeeDto;
    }

    private List<RoleDto> convertRolesListToRolesDtoList(Set<Role> roles) {
        return roles.stream().map(this::convertRoleToRoleDTO).collect(Collectors.toList());
    }

    private RoleDto convertRoleToRoleDTO(Role role) {
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(role, roleDto);
        return roleDto;
    }

    private List<EmployeeListDto> convertEmployeeListToEmployeeListDto(List<Employee> employeeList) {
        return employeeList.stream().map(this::convertEmployeeToEmployeeListDto).collect(Collectors.toList());
    }
}
