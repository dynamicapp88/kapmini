package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.AuditLog;
import io.ankush.kap_mini.domain.Collaborator;
import io.ankush.kap_mini.domain.History;
import io.ankush.kap_mini.domain.Record;
import io.ankush.kap_mini.domain.RolePermission;
import io.ankush.kap_mini.domain.SubDomain;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.domain.UserRole;
import io.ankush.kap_mini.domain.UserSetting;
import io.ankush.kap_mini.model.UserDTO;
import io.ankush.kap_mini.repos.AuditLogRepository;
import io.ankush.kap_mini.repos.CollaboratorRepository;
import io.ankush.kap_mini.repos.HistoryRepository;
import io.ankush.kap_mini.repos.RecordRepository;
import io.ankush.kap_mini.repos.RolePermissionRepository;
import io.ankush.kap_mini.repos.SubDomainRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.repos.UserRoleRepository;
import io.ankush.kap_mini.repos.UserSettingRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubDomainRepository subDomainRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final RecordRepository recordRepository;
    private final UserSettingRepository userSettingRepository;
    private final HistoryRepository historyRepository;
    private final AuditLogRepository auditLogRepository;

    public UserService(final UserRepository userRepository,
            final SubDomainRepository subDomainRepository,
            final RolePermissionRepository rolePermissionRepository,
            final UserRoleRepository userRoleRepository,
            final CollaboratorRepository collaboratorRepository,
            final RecordRepository recordRepository,
            final UserSettingRepository userSettingRepository,
            final HistoryRepository historyRepository,
            final AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.subDomainRepository = subDomainRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRoleRepository = userRoleRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.recordRepository = recordRepository;
        this.userSettingRepository = userSettingRepository;
        this.historyRepository = historyRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final UUID userId) {
        return userRepository.findById(userId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUserId();
    }

    public void update(final UUID userId, final UserDTO userDTO) {
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final UUID userId) {
        userRepository.deleteById(userId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserId(user.getUserId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setIsActive(user.getIsActive());
        userDTO.setPassword(user.getPassword());
        userDTO.setDeletedAt(user.getDeletedAt());
        userDTO.setStatus(user.getStatus());
        userDTO.setSubdomainID(user.getSubdomainID() == null ? null : user.getSubdomainID().getSubdomain());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setIsActive(userDTO.getIsActive());
        user.setPassword(userDTO.getPassword());
        user.setDeletedAt(userDTO.getDeletedAt());
        user.setStatus(userDTO.getStatus());
        final SubDomain subdomainID = userDTO.getSubdomainID() == null ? null : subDomainRepository.findById(userDTO.getSubdomainID())
                .orElseThrow(() -> new NotFoundException("subdomainID not found"));
        user.setSubdomainID(subdomainID);
        return user;
    }

    public ReferencedWarning getReferencedWarning(final UUID userId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        final RolePermission roleIdRolePermission = rolePermissionRepository.findFirstByRoleId(user);
        if (roleIdRolePermission != null) {
            referencedWarning.setKey("user.rolePermission.roleId.referenced");
            referencedWarning.addParam(roleIdRolePermission.getRolePermissionId());
            return referencedWarning;
        }
        final UserRole userIdUserRole = userRoleRepository.findFirstByUserId(user);
        if (userIdUserRole != null) {
            referencedWarning.setKey("user.userRole.userId.referenced");
            referencedWarning.addParam(userIdUserRole.getUserRoleId());
            return referencedWarning;
        }
        final Collaborator userIDCollaborator = collaboratorRepository.findFirstByUserID(user);
        if (userIDCollaborator != null) {
            referencedWarning.setKey("user.collaborator.userID.referenced");
            referencedWarning.addParam(userIDCollaborator.getCollaboratorID());
            return referencedWarning;
        }
        final Record createdByRecord = recordRepository.findFirstByCreatedBy(user);
        if (createdByRecord != null) {
            referencedWarning.setKey("user.record.createdBy.referenced");
            referencedWarning.addParam(createdByRecord.getRecordId());
            return referencedWarning;
        }
        final Record updatedByRecord = recordRepository.findFirstByUpdatedBy(user);
        if (updatedByRecord != null) {
            referencedWarning.setKey("user.record.updatedBy.referenced");
            referencedWarning.addParam(updatedByRecord.getRecordId());
            return referencedWarning;
        }
        final UserSetting userIdUserSetting = userSettingRepository.findFirstByUserId(user);
        if (userIdUserSetting != null) {
            referencedWarning.setKey("user.userSetting.userId.referenced");
            referencedWarning.addParam(userIdUserSetting.getSettingID());
            return referencedWarning;
        }
        final History userIdHistory = historyRepository.findFirstByUserId(user);
        if (userIdHistory != null) {
            referencedWarning.setKey("user.history.userId.referenced");
            referencedWarning.addParam(userIdHistory.getHistoryID());
            return referencedWarning;
        }
        final AuditLog userIdAuditLog = auditLogRepository.findFirstByUserId(user);
        if (userIdAuditLog != null) {
            referencedWarning.setKey("user.auditLog.userId.referenced");
            referencedWarning.addParam(userIdAuditLog.getId());
            return referencedWarning;
        }
        return null;
    }

}
