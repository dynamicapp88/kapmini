package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Permission;
import io.ankush.kap_mini.domain.RolePermission;
import io.ankush.kap_mini.model.PermissionDTO;
import io.ankush.kap_mini.repos.PermissionRepository;
import io.ankush.kap_mini.repos.RolePermissionRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public PermissionService(final PermissionRepository permissionRepository,
            final RolePermissionRepository rolePermissionRepository) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public List<PermissionDTO> findAll() {
        final List<Permission> permissions = permissionRepository.findAll(Sort.by("premissionId"));
        return permissions.stream()
                .map(permission -> mapToDTO(permission, new PermissionDTO()))
                .toList();
    }

    public PermissionDTO get(final UUID premissionId) {
        return permissionRepository.findById(premissionId)
                .map(permission -> mapToDTO(permission, new PermissionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final PermissionDTO permissionDTO) {
        final Permission permission = new Permission();
        mapToEntity(permissionDTO, permission);
        return permissionRepository.save(permission).getPremissionId();
    }

    public void update(final UUID premissionId, final PermissionDTO permissionDTO) {
        final Permission permission = permissionRepository.findById(premissionId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(permissionDTO, permission);
        permissionRepository.save(permission);
    }

    public void delete(final UUID premissionId) {
        permissionRepository.deleteById(premissionId);
    }

    private PermissionDTO mapToDTO(final Permission permission, final PermissionDTO permissionDTO) {
        permissionDTO.setPremissionId(permission.getPremissionId());
        permissionDTO.setName(permission.getName());
        permissionDTO.setDescription(permission.getDescription());
        return permissionDTO;
    }

    private Permission mapToEntity(final PermissionDTO permissionDTO, final Permission permission) {
        permission.setName(permissionDTO.getName());
        permission.setDescription(permissionDTO.getDescription());
        return permission;
    }

    public ReferencedWarning getReferencedWarning(final UUID premissionId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Permission permission = permissionRepository.findById(premissionId)
                .orElseThrow(NotFoundException::new);
        final RolePermission premissionIdRolePermission = rolePermissionRepository.findFirstByPremissionId(permission);
        if (premissionIdRolePermission != null) {
            referencedWarning.setKey("permission.rolePermission.premissionId.referenced");
            referencedWarning.addParam(premissionIdRolePermission.getRolePermissionId());
            return referencedWarning;
        }
        return null;
    }

}
