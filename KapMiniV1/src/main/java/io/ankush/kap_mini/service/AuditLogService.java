package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.AuditLog;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.AuditLogDTO;
import io.ankush.kap_mini.repos.AuditLogRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(final AuditLogRepository auditLogRepository,
            final UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public List<AuditLogDTO> findAll() {
        final List<AuditLog> auditLogs = auditLogRepository.findAll(Sort.by("id"));
        return auditLogs.stream()
                .map(auditLog -> mapToDTO(auditLog, new AuditLogDTO()))
                .toList();
    }

    public AuditLogDTO get(final UUID id) {
        return auditLogRepository.findById(id)
                .map(auditLog -> mapToDTO(auditLog, new AuditLogDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final AuditLogDTO auditLogDTO) {
        final AuditLog auditLog = new AuditLog();
        mapToEntity(auditLogDTO, auditLog);
        return auditLogRepository.save(auditLog).getId();
    }

    public void update(final UUID id, final AuditLogDTO auditLogDTO) {
        final AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(auditLogDTO, auditLog);
        auditLogRepository.save(auditLog);
    }

    public void delete(final UUID id) {
        auditLogRepository.deleteById(id);
    }

    private AuditLogDTO mapToDTO(final AuditLog auditLog, final AuditLogDTO auditLogDTO) {
        auditLogDTO.setId(auditLog.getId());
        auditLogDTO.setEntityName(auditLog.getEntityName());
        auditLogDTO.setEntityID(auditLog.getEntityID());
        auditLogDTO.setActionType(auditLog.getActionType());
        auditLogDTO.setActionDetails(auditLog.getActionDetails());
        auditLogDTO.setPerformedBy(auditLog.getPerformedBy());
        auditLogDTO.setIp(auditLog.getIp());
        auditLogDTO.setSessionID(auditLog.getSessionID());
        auditLogDTO.setUserAgent(auditLog.getUserAgent());
        auditLogDTO.setLastLoginAt(auditLog.getLastLoginAt());
        auditLogDTO.setLastActivityAt(auditLog.getLastActivityAt());
        auditLogDTO.setActionOutcome(auditLog.getActionOutcome());
        auditLogDTO.setProcessingTime(auditLog.getProcessingTime());
        auditLogDTO.setUserId(auditLog.getUserId() == null ? null : auditLog.getUserId().getUserId());
        return auditLogDTO;
    }

    private AuditLog mapToEntity(final AuditLogDTO auditLogDTO, final AuditLog auditLog) {
        auditLog.setEntityName(auditLogDTO.getEntityName());
        auditLog.setEntityID(auditLogDTO.getEntityID());
        auditLog.setActionType(auditLogDTO.getActionType());
        auditLog.setActionDetails(auditLogDTO.getActionDetails());
        auditLog.setPerformedBy(auditLogDTO.getPerformedBy());
        auditLog.setIp(auditLogDTO.getIp());
        auditLog.setSessionID(auditLogDTO.getSessionID());
        auditLog.setUserAgent(auditLogDTO.getUserAgent());
        auditLog.setLastLoginAt(auditLogDTO.getLastLoginAt());
        auditLog.setLastActivityAt(auditLogDTO.getLastActivityAt());
        auditLog.setActionOutcome(auditLogDTO.getActionOutcome());
        auditLog.setProcessingTime(auditLogDTO.getProcessingTime());
        final User userId = auditLogDTO.getUserId() == null ? null : userRepository.findById(auditLogDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        auditLog.setUserId(userId);
        return auditLog;
    }

}
