package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.EmailConfig;
import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.model.EmailConfigDTO;
import io.ankush.kap_mini.repos.EmailConfigRepository;
import io.ankush.kap_mini.repos.NotificationConfigurationTypeRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EmailConfigService {

    private final EmailConfigRepository emailConfigRepository;
    private final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository;

    public EmailConfigService(final EmailConfigRepository emailConfigRepository,
            final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository) {
        this.emailConfigRepository = emailConfigRepository;
        this.notificationConfigurationTypeRepository = notificationConfigurationTypeRepository;
    }

    public List<EmailConfigDTO> findAll() {
        final List<EmailConfig> emailConfigs = emailConfigRepository.findAll(Sort.by("configId"));
        return emailConfigs.stream()
                .map(emailConfig -> mapToDTO(emailConfig, new EmailConfigDTO()))
                .toList();
    }

    public EmailConfigDTO get(final UUID configId) {
        return emailConfigRepository.findById(configId)
                .map(emailConfig -> mapToDTO(emailConfig, new EmailConfigDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final EmailConfigDTO emailConfigDTO) {
        final EmailConfig emailConfig = new EmailConfig();
        mapToEntity(emailConfigDTO, emailConfig);
        return emailConfigRepository.save(emailConfig).getConfigId();
    }

    public void update(final UUID configId, final EmailConfigDTO emailConfigDTO) {
        final EmailConfig emailConfig = emailConfigRepository.findById(configId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(emailConfigDTO, emailConfig);
        emailConfigRepository.save(emailConfig);
    }

    public void delete(final UUID configId) {
        emailConfigRepository.deleteById(configId);
    }

    private EmailConfigDTO mapToDTO(final EmailConfig emailConfig,
            final EmailConfigDTO emailConfigDTO) {
        emailConfigDTO.setConfigId(emailConfig.getConfigId());
        emailConfigDTO.setSmtpServer(emailConfig.getSmtpServer());
        emailConfigDTO.setPort(emailConfig.getPort());
        emailConfigDTO.setAuthRequired(emailConfig.getAuthRequired());
        emailConfigDTO.setDeleteAt(emailConfig.getDeleteAt());
        emailConfigDTO.setNotificationTypeID(emailConfig.getNotificationTypeID() == null ? null : emailConfig.getNotificationTypeID().getConfigTypeID());
        return emailConfigDTO;
    }

    private EmailConfig mapToEntity(final EmailConfigDTO emailConfigDTO,
            final EmailConfig emailConfig) {
        emailConfig.setSmtpServer(emailConfigDTO.getSmtpServer());
        emailConfig.setPort(emailConfigDTO.getPort());
        emailConfig.setAuthRequired(emailConfigDTO.getAuthRequired());
        emailConfig.setDeleteAt(emailConfigDTO.getDeleteAt());
        final NotificationConfigurationType notificationTypeID = emailConfigDTO.getNotificationTypeID() == null ? null : notificationConfigurationTypeRepository.findById(emailConfigDTO.getNotificationTypeID())
                .orElseThrow(() -> new NotFoundException("notificationTypeID not found"));
        emailConfig.setNotificationTypeID(notificationTypeID);
        return emailConfig;
    }

}
