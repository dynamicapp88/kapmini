package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.InAppConfig;
import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.model.InAppConfigDTO;
import io.ankush.kap_mini.repos.InAppConfigRepository;
import io.ankush.kap_mini.repos.NotificationConfigurationTypeRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class InAppConfigService {

    private final InAppConfigRepository inAppConfigRepository;
    private final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository;

    public InAppConfigService(final InAppConfigRepository inAppConfigRepository,
            final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository) {
        this.inAppConfigRepository = inAppConfigRepository;
        this.notificationConfigurationTypeRepository = notificationConfigurationTypeRepository;
    }

    public List<InAppConfigDTO> findAll() {
        final List<InAppConfig> inAppConfigs = inAppConfigRepository.findAll(Sort.by("configID"));
        return inAppConfigs.stream()
                .map(inAppConfig -> mapToDTO(inAppConfig, new InAppConfigDTO()))
                .toList();
    }

    public InAppConfigDTO get(final UUID configID) {
        return inAppConfigRepository.findById(configID)
                .map(inAppConfig -> mapToDTO(inAppConfig, new InAppConfigDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final InAppConfigDTO inAppConfigDTO) {
        final InAppConfig inAppConfig = new InAppConfig();
        mapToEntity(inAppConfigDTO, inAppConfig);
        return inAppConfigRepository.save(inAppConfig).getConfigID();
    }

    public void update(final UUID configID, final InAppConfigDTO inAppConfigDTO) {
        final InAppConfig inAppConfig = inAppConfigRepository.findById(configID)
                .orElseThrow(NotFoundException::new);
        mapToEntity(inAppConfigDTO, inAppConfig);
        inAppConfigRepository.save(inAppConfig);
    }

    public void delete(final UUID configID) {
        inAppConfigRepository.deleteById(configID);
    }

    private InAppConfigDTO mapToDTO(final InAppConfig inAppConfig,
            final InAppConfigDTO inAppConfigDTO) {
        inAppConfigDTO.setConfigID(inAppConfig.getConfigID());
        inAppConfigDTO.setDisplayDuration(inAppConfig.getDisplayDuration());
        inAppConfigDTO.setPriority(inAppConfig.getPriority());
        inAppConfigDTO.setDeleteAt(inAppConfig.getDeleteAt());
        inAppConfigDTO.setNotificationTypeID(inAppConfig.getNotificationTypeID() == null ? null : inAppConfig.getNotificationTypeID().getConfigTypeID());
        return inAppConfigDTO;
    }

    private InAppConfig mapToEntity(final InAppConfigDTO inAppConfigDTO,
            final InAppConfig inAppConfig) {
        inAppConfig.setDisplayDuration(inAppConfigDTO.getDisplayDuration());
        inAppConfig.setPriority(inAppConfigDTO.getPriority());
        inAppConfig.setDeleteAt(inAppConfigDTO.getDeleteAt());
        final NotificationConfigurationType notificationTypeID = inAppConfigDTO.getNotificationTypeID() == null ? null : notificationConfigurationTypeRepository.findById(inAppConfigDTO.getNotificationTypeID())
                .orElseThrow(() -> new NotFoundException("notificationTypeID not found"));
        inAppConfig.setNotificationTypeID(notificationTypeID);
        return inAppConfig;
    }

}
