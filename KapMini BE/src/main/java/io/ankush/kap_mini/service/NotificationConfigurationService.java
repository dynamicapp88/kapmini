package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.NotificationConfiguration;
import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.model.NotificationConfigurationDTO;
import io.ankush.kap_mini.repos.NotificationConfigurationRepository;
import io.ankush.kap_mini.repos.NotificationConfigurationTypeRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NotificationConfigurationService {

    private final NotificationConfigurationRepository notificationConfigurationRepository;
    private final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository;

    public NotificationConfigurationService(
            final NotificationConfigurationRepository notificationConfigurationRepository,
            final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository) {
        this.notificationConfigurationRepository = notificationConfigurationRepository;
        this.notificationConfigurationTypeRepository = notificationConfigurationTypeRepository;
    }

    public List<NotificationConfigurationDTO> findAll() {
        final List<NotificationConfiguration> notificationConfigurations = notificationConfigurationRepository.findAll(Sort.by("configID"));
        return notificationConfigurations.stream()
                .map(notificationConfiguration -> mapToDTO(notificationConfiguration, new NotificationConfigurationDTO()))
                .toList();
    }

    public NotificationConfigurationDTO get(final UUID configID) {
        return notificationConfigurationRepository.findById(configID)
                .map(notificationConfiguration -> mapToDTO(notificationConfiguration, new NotificationConfigurationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final NotificationConfigurationDTO notificationConfigurationDTO) {
        final NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        mapToEntity(notificationConfigurationDTO, notificationConfiguration);
        return notificationConfigurationRepository.save(notificationConfiguration).getConfigID();
    }

    public void update(final UUID configID,
            final NotificationConfigurationDTO notificationConfigurationDTO) {
        final NotificationConfiguration notificationConfiguration = notificationConfigurationRepository.findById(configID)
                .orElseThrow(NotFoundException::new);
        mapToEntity(notificationConfigurationDTO, notificationConfiguration);
        notificationConfigurationRepository.save(notificationConfiguration);
    }

    public void delete(final UUID configID) {
        notificationConfigurationRepository.deleteById(configID);
    }

    private NotificationConfigurationDTO mapToDTO(
            final NotificationConfiguration notificationConfiguration,
            final NotificationConfigurationDTO notificationConfigurationDTO) {
        notificationConfigurationDTO.setConfigID(notificationConfiguration.getConfigID());
        notificationConfigurationDTO.setName(notificationConfiguration.getName());
        notificationConfigurationDTO.setIsAction(notificationConfiguration.getIsAction());
        notificationConfigurationDTO.setDescription(notificationConfiguration.getDescription());
        notificationConfigurationDTO.setDeleteAt(notificationConfiguration.getDeleteAt());
        return notificationConfigurationDTO;
    }

    private NotificationConfiguration mapToEntity(
            final NotificationConfigurationDTO notificationConfigurationDTO,
            final NotificationConfiguration notificationConfiguration) {
        notificationConfiguration.setName(notificationConfigurationDTO.getName());
        notificationConfiguration.setIsAction(notificationConfigurationDTO.getIsAction());
        notificationConfiguration.setDescription(notificationConfigurationDTO.getDescription());
        notificationConfiguration.setDeleteAt(notificationConfigurationDTO.getDeleteAt());
        return notificationConfiguration;
    }

    public ReferencedWarning getReferencedWarning(final UUID configID) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final NotificationConfiguration notificationConfiguration = notificationConfigurationRepository.findById(configID)
                .orElseThrow(NotFoundException::new);
        final NotificationConfigurationType configIdNotificationConfigurationType = notificationConfigurationTypeRepository.findFirstByConfigId(notificationConfiguration);
        if (configIdNotificationConfigurationType != null) {
            referencedWarning.setKey("notificationConfiguration.notificationConfigurationType.configId.referenced");
            referencedWarning.addParam(configIdNotificationConfigurationType.getConfigTypeID());
            return referencedWarning;
        }
        return null;
    }

}
