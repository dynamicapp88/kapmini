package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Role;
import io.ankush.kap_mini.domain.UserRole;
import io.ankush.kap_mini.model.RoleDTO;
import io.ankush.kap_mini.repos.RoleRepository;
import io.ankush.kap_mini.repos.UserRoleRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public RoleService(final RoleRepository roleRepository,
            final UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public List<RoleDTO> findAll() {
        final List<Role> roles = roleRepository.findAll(Sort.by("roleId"));
        return roles.stream()
                .map(role -> mapToDTO(role, new RoleDTO()))
                .toList();
    }

    public RoleDTO get(final UUID roleId) {
        return roleRepository.findById(roleId)
                .map(role -> mapToDTO(role, new RoleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final RoleDTO roleDTO) {
        final Role role = new Role();
        mapToEntity(roleDTO, role);
        return roleRepository.save(role).getRoleId();
    }

    public void update(final UUID roleId, final RoleDTO roleDTO) {
        final Role role = roleRepository.findById(roleId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(roleDTO, role);
        roleRepository.save(role);
    }

    public void delete(final UUID roleId) {
        roleRepository.deleteById(roleId);
    }

    private RoleDTO mapToDTO(final Role role, final RoleDTO roleDTO) {
        roleDTO.setRoleId(role.getRoleId());
        roleDTO.setRoleName(role.getRoleName());
        roleDTO.setRoleDescription(role.getRoleDescription());
        roleDTO.setDeletedAt(role.getDeletedAt());
        return roleDTO;
    }

    private Role mapToEntity(final RoleDTO roleDTO, final Role role) {
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleDescription(roleDTO.getRoleDescription());
        role.setDeletedAt(roleDTO.getDeletedAt());
        return role;
    }

    public ReferencedWarning getReferencedWarning(final UUID roleId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Role role = roleRepository.findById(roleId)
                .orElseThrow(NotFoundException::new);
        final UserRole roleIdUserRole = userRoleRepository.findFirstByRoleId(role);
        if (roleIdUserRole != null) {
            referencedWarning.setKey("role.userRole.roleId.referenced");
            referencedWarning.addParam(roleIdUserRole.getUserRoleId());
            return referencedWarning;
        }
        return null;
    }

}
