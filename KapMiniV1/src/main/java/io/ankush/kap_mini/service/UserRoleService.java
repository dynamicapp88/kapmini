package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Role;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.domain.UserRole;
import io.ankush.kap_mini.model.UserRoleDTO;
import io.ankush.kap_mini.repos.RoleRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.repos.UserRoleRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleService(final UserRoleRepository userRoleRepository,
            final UserRepository userRepository, final RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserRoleDTO> findAll() {
        final List<UserRole> userRoles = userRoleRepository.findAll(Sort.by("userRoleId"));
        return userRoles.stream()
                .map(userRole -> mapToDTO(userRole, new UserRoleDTO()))
                .toList();
    }

    public UserRoleDTO get(final UUID userRoleId) {
        return userRoleRepository.findById(userRoleId)
                .map(userRole -> mapToDTO(userRole, new UserRoleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UserRoleDTO userRoleDTO) {
        final UserRole userRole = new UserRole();
        mapToEntity(userRoleDTO, userRole);
        return userRoleRepository.save(userRole).getUserRoleId();
    }

    public void update(final UUID userRoleId, final UserRoleDTO userRoleDTO) {
        final UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userRoleDTO, userRole);
        userRoleRepository.save(userRole);
    }

    public void delete(final UUID userRoleId) {
        userRoleRepository.deleteById(userRoleId);
    }

    private UserRoleDTO mapToDTO(final UserRole userRole, final UserRoleDTO userRoleDTO) {
        userRoleDTO.setUserRoleId(userRole.getUserRoleId());
        userRoleDTO.setDeleteAt(userRole.getDeleteAt());
        userRoleDTO.setUserId(userRole.getUserId() == null ? null : userRole.getUserId().getUserId());
        userRoleDTO.setRoleId(userRole.getRoleId() == null ? null : userRole.getRoleId().getRoleId());
        return userRoleDTO;
    }

    private UserRole mapToEntity(final UserRoleDTO userRoleDTO, final UserRole userRole) {
        userRole.setDeleteAt(userRoleDTO.getDeleteAt());
        final User userId = userRoleDTO.getUserId() == null ? null : userRepository.findById(userRoleDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        userRole.setUserId(userId);
        final Role roleId = userRoleDTO.getRoleId() == null ? null : roleRepository.findById(userRoleDTO.getRoleId())
                .orElseThrow(() -> new NotFoundException("roleId not found"));
        userRole.setRoleId(roleId);
        return userRole;
    }

}
