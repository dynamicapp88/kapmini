package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.domain.SMSConfig;
import io.ankush.kap_mini.model.SMSConfigDTO;
import io.ankush.kap_mini.repos.NotificationConfigurationTypeRepository;
import io.ankush.kap_mini.repos.SMSConfigRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SMSConfigService {

    private final SMSConfigRepository sMSConfigRepository;
    private final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository;

    public SMSConfigService(final SMSConfigRepository sMSConfigRepository,
            final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository) {
        this.sMSConfigRepository = sMSConfigRepository;
        this.notificationConfigurationTypeRepository = notificationConfigurationTypeRepository;
    }

    public List<SMSConfigDTO> findAll() {
        final List<SMSConfig> sMSConfigs = sMSConfigRepository.findAll(Sort.by("configId"));
        return sMSConfigs.stream()
                .map(sMSConfig -> mapToDTO(sMSConfig, new SMSConfigDTO()))
                .toList();
    }

    public SMSConfigDTO get(final UUID configId) {
        return sMSConfigRepository.findById(configId)
                .map(sMSConfig -> mapToDTO(sMSConfig, new SMSConfigDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final SMSConfigDTO sMSConfigDTO) {
        final SMSConfig sMSConfig = new SMSConfig();
        mapToEntity(sMSConfigDTO, sMSConfig);
        return sMSConfigRepository.save(sMSConfig).getConfigId();
    }

    public void update(final UUID configId, final SMSConfigDTO sMSConfigDTO) {
        final SMSConfig sMSConfig = sMSConfigRepository.findById(configId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(sMSConfigDTO, sMSConfig);
        sMSConfigRepository.save(sMSConfig);
    }

    public void delete(final UUID configId) {
        sMSConfigRepository.deleteById(configId);
    }

    private SMSConfigDTO mapToDTO(final SMSConfig sMSConfig, final SMSConfigDTO sMSConfigDTO) {
        sMSConfigDTO.setConfigId(sMSConfig.getConfigId());
        sMSConfigDTO.setSmsApiEndpoint(sMSConfig.getSmsApiEndpoint());
        sMSConfigDTO.setAuthKey(sMSConfig.getAuthKey());
        sMSConfigDTO.setSenderNumber(sMSConfig.getSenderNumber());
        sMSConfigDTO.setDeleteAt(sMSConfig.getDeleteAt());
        sMSConfigDTO.setNotificationTypeID(sMSConfig.getNotificationTypeID() == null ? null : sMSConfig.getNotificationTypeID().getConfigTypeID());
        return sMSConfigDTO;
    }

    private SMSConfig mapToEntity(final SMSConfigDTO sMSConfigDTO, final SMSConfig sMSConfig) {
        sMSConfig.setSmsApiEndpoint(sMSConfigDTO.getSmsApiEndpoint());
        sMSConfig.setAuthKey(sMSConfigDTO.getAuthKey());
        sMSConfig.setSenderNumber(sMSConfigDTO.getSenderNumber());
        sMSConfig.setDeleteAt(sMSConfigDTO.getDeleteAt());
        final NotificationConfigurationType notificationTypeID = sMSConfigDTO.getNotificationTypeID() == null ? null : notificationConfigurationTypeRepository.findById(sMSConfigDTO.getNotificationTypeID())
                .orElseThrow(() -> new NotFoundException("notificationTypeID not found"));
        sMSConfig.setNotificationTypeID(notificationTypeID);
        return sMSConfig;
    }

}
