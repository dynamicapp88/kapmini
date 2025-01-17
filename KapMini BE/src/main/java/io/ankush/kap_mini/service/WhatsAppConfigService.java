package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.domain.WhatsAppConfig;
import io.ankush.kap_mini.model.WhatsAppConfigDTO;
import io.ankush.kap_mini.repos.NotificationConfigurationTypeRepository;
import io.ankush.kap_mini.repos.WhatsAppConfigRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class WhatsAppConfigService {

    private final WhatsAppConfigRepository whatsAppConfigRepository;
    private final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository;

    public WhatsAppConfigService(final WhatsAppConfigRepository whatsAppConfigRepository,
            final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository) {
        this.whatsAppConfigRepository = whatsAppConfigRepository;
        this.notificationConfigurationTypeRepository = notificationConfigurationTypeRepository;
    }

    public List<WhatsAppConfigDTO> findAll() {
        final List<WhatsAppConfig> whatsAppConfigs = whatsAppConfigRepository.findAll(Sort.by("configID"));
        return whatsAppConfigs.stream()
                .map(whatsAppConfig -> mapToDTO(whatsAppConfig, new WhatsAppConfigDTO()))
                .toList();
    }

    public WhatsAppConfigDTO get(final Long configID) {
        return whatsAppConfigRepository.findById(configID)
                .map(whatsAppConfig -> mapToDTO(whatsAppConfig, new WhatsAppConfigDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final WhatsAppConfigDTO whatsAppConfigDTO) {
        final WhatsAppConfig whatsAppConfig = new WhatsAppConfig();
        mapToEntity(whatsAppConfigDTO, whatsAppConfig);
        return whatsAppConfigRepository.save(whatsAppConfig).getConfigID();
    }

    public void update(final Long configID, final WhatsAppConfigDTO whatsAppConfigDTO) {
        final WhatsAppConfig whatsAppConfig = whatsAppConfigRepository.findById(configID)
                .orElseThrow(NotFoundException::new);
        mapToEntity(whatsAppConfigDTO, whatsAppConfig);
        whatsAppConfigRepository.save(whatsAppConfig);
    }

    public void delete(final Long configID) {
        whatsAppConfigRepository.deleteById(configID);
    }

    private WhatsAppConfigDTO mapToDTO(final WhatsAppConfig whatsAppConfig,
            final WhatsAppConfigDTO whatsAppConfigDTO) {
        whatsAppConfigDTO.setConfigID(whatsAppConfig.getConfigID());
        whatsAppConfigDTO.setWhatsappApiEndpoint(whatsAppConfig.getWhatsappApiEndpoint());
        whatsAppConfigDTO.setAuthKey(whatsAppConfig.getAuthKey());
        whatsAppConfigDTO.setBusinessNumber(whatsAppConfig.getBusinessNumber());
        whatsAppConfigDTO.setDeleteAt(whatsAppConfig.getDeleteAt());
        whatsAppConfigDTO.setNotificationTypeID(whatsAppConfig.getNotificationTypeID() == null ? null : whatsAppConfig.getNotificationTypeID().getConfigTypeID());
        return whatsAppConfigDTO;
    }

    private WhatsAppConfig mapToEntity(final WhatsAppConfigDTO whatsAppConfigDTO,
            final WhatsAppConfig whatsAppConfig) {
        whatsAppConfig.setWhatsappApiEndpoint(whatsAppConfigDTO.getWhatsappApiEndpoint());
        whatsAppConfig.setAuthKey(whatsAppConfigDTO.getAuthKey());
        whatsAppConfig.setBusinessNumber(whatsAppConfigDTO.getBusinessNumber());
        whatsAppConfig.setDeleteAt(whatsAppConfigDTO.getDeleteAt());
        final NotificationConfigurationType notificationTypeID = whatsAppConfigDTO.getNotificationTypeID() == null ? null : notificationConfigurationTypeRepository.findById(whatsAppConfigDTO.getNotificationTypeID())
                .orElseThrow(() -> new NotFoundException("notificationTypeID not found"));
        whatsAppConfig.setNotificationTypeID(notificationTypeID);
        return whatsAppConfig;
    }

}
