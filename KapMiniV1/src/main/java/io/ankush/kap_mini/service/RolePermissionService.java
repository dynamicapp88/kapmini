package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Permission;
import io.ankush.kap_mini.domain.RolePermission;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.RolePermissionDTO;
import io.ankush.kap_mini.repos.PermissionRepository;
import io.ankush.kap_mini.repos.RolePermissionRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public RolePermissionService(final RolePermissionRepository rolePermissionRepository,
            final UserRepository userRepository, final PermissionRepository permissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<RolePermissionDTO> findAll() {
        final List<RolePermission> rolePermissions = rolePermissionRepository.findAll(Sort.by("rolePermissionId"));
        return rolePermissions.stream()
                .map(rolePermission -> mapToDTO(rolePermission, new RolePermissionDTO()))
                .toList();
    }

    public RolePermissionDTO get(final UUID rolePermissionId) {
        return rolePermissionRepository.findById(rolePermissionId)
                .map(rolePermission -> mapToDTO(rolePermission, new RolePermissionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final RolePermissionDTO rolePermissionDTO) {
        final RolePermission rolePermission = new RolePermission();
        mapToEntity(rolePermissionDTO, rolePermission);
        return rolePermissionRepository.save(rolePermission).getRolePermissionId();
    }

    public void update(final UUID rolePermissionId, final RolePermissionDTO rolePermissionDTO) {
        final RolePermission rolePermission = rolePermissionRepository.findById(rolePermissionId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(rolePermissionDTO, rolePermission);
        rolePermissionRepository.save(rolePermission);
    }

    public void delete(final UUID rolePermissionId) {
        rolePermissionRepository.deleteById(rolePermissionId);
    }

    private RolePermissionDTO mapToDTO(final RolePermission rolePermission,
            final RolePermissionDTO rolePermissionDTO) {
        rolePermissionDTO.setRolePermissionId(rolePermission.getRolePermissionId());
        rolePermissionDTO.setRoleId(rolePermission.getRoleId() == null ? null : rolePermission.getRoleId().getUserId());
        rolePermissionDTO.setPremissionId(rolePermission.getPremissionId() == null ? null : rolePermission.getPremissionId().getPremissionId());
        return rolePermissionDTO;
    }

    private RolePermission mapToEntity(final RolePermissionDTO rolePermissionDTO,
            final RolePermission rolePermission) {
        final User roleId = rolePermissionDTO.getRoleId() == null ? null : userRepository.findById(rolePermissionDTO.getRoleId())
                .orElseThrow(() -> new NotFoundException("roleId not found"));
        rolePermission.setRoleId(roleId);
        final Permission premissionId = rolePermissionDTO.getPremissionId() == null ? null : permissionRepository.findById(rolePermissionDTO.getPremissionId())
                .orElseThrow(() -> new NotFoundException("premissionId not found"));
        rolePermission.setPremissionId(premissionId);
        return rolePermission;
    }

}
