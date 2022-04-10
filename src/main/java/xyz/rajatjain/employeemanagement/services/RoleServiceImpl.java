package xyz.rajatjain.employeemanagement.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.rajatjain.employeemanagement.dtos.ResponseObj;
import xyz.rajatjain.employeemanagement.dtos.RoleDto;
import xyz.rajatjain.employeemanagement.models.Role;
import xyz.rajatjain.employeemanagement.repositories.RoleRepository;
import xyz.rajatjain.employeemanagement.utils.CommonUtils;
import xyz.rajatjain.employeemanagement.utils.Constants;
import xyz.rajatjain.employeemanagement.utils.ResponseUtil;

import java.util.Optional;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<ResponseObj> createUpdateRole(RoleDto roleDto) {
        try {
            Role role;
            if (roleDto.getId() == null) {
                role = new Role();
                role.setCreationDate(CommonUtils.getCurrentTimestamp());
            } else {
                Optional<Role> roleOptional = roleRepository.findById(roleDto.getId());
                if (roleOptional.isEmpty()) {
                    return ResponseUtil.getFailureResponse("Role not found with ID");
                }
                role = roleOptional.get();
            }

            Optional<Role> roleByNameOptional = roleRepository.findByName(roleDto.getName().trim());
            if (roleByNameOptional.isPresent() && roleDto.getId() != null &&
                    roleByNameOptional.get().getId().longValue() != roleDto.getId().longValue()) {
                return ResponseUtil.getFailureResponse("A role with the same name already exists");
            }

            role.setUpdateDate(CommonUtils.getCurrentTimestamp());
            BeanUtils.copyProperties(roleDto, role, CommonUtils.getNullPropertyNames(roleDto));

            role = roleRepository.save(role);

            return ResponseUtil.getSuccessResponse(role, Constants.DATA_SAVE_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("Error in createUpdateRole", e);
            return ResponseUtil.getErrorResponse(e);
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllRoles() {
        try {
            return ResponseUtil.getSuccessResponse(roleRepository.findAll(), Constants.GET_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("Error in createUpdateRole", e);
            return ResponseUtil.getErrorResponse(e);
        }
    }

    @Override
    public Role checkAndCreateRole(String role) {
        Optional<Role> roleAdminCheck = roleRepository.findByName(role);
        if (roleAdminCheck.isEmpty()) {
            Role roleAdmin =
                    Role.builder().name(role).creationDate(CommonUtils.getCurrentTimestamp()).updateDate(CommonUtils.getCurrentTimestamp()).build();
            roleRepository.save(roleAdmin);
            log.info("Role " + role + " Created ID - " + roleAdmin.getId());
            return roleAdmin;
        } else {
            log.info("Role " + role + " OK!");
            return roleAdminCheck.get();
        }
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow();
    }
}
