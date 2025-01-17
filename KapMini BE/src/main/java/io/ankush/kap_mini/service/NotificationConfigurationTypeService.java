package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.EmailConfig;
import io.ankush.kap_mini.domain.InAppConfig;
import io.ankush.kap_mini.domain.NotificationConfiguration;
import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.domain.SMSConfig;
import io.ankush.kap_mini.domain.WhatsAppConfig;
import io.ankush.kap_mini.model.NotificationConfigurationTypeDTO;
import io.ankush.kap_mini.repos.EmailConfigRepository;
import io.ankush.kap_mini.repos.InAppConfigRepository;
import io.ankush.kap_mini.repos.NotificationConfigurationRepository;
import io.ankush.kap_mini.repos.NotificationConfigurationTypeRepository;
import io.ankush.kap_mini.repos.SMSConfigRepository;
import io.ankush.kap_mini.repos.WhatsAppConfigRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NotificationConfigurationTypeService {

    private final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository;
    private final NotificationConfigurationRepository notificationConfigurationRepository;
    private final EmailConfigRepository emailConfigRepository;
    private final SMSConfigRepository sMSConfigRepository;
    private final InAppConfigRepository inAppConfigRepository;
    private final WhatsAppConfigRepository whatsAppConfigRepository;

    public NotificationConfigurationTypeService(
            final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository,
            final NotificationConfigurationRepository notificationConfigurationRepository,
            final EmailConfigRepository emailConfigRepository,
            final SMSConfigRepository sMSConfigRepository,
            final InAppConfigRepository inAppConfigRepository,
            final WhatsAppConfigRepository whatsAppConfigRepository) {
        this.notificationConfigurationTypeRepository = notificationConfigurationTypeRepository;
        this.notificationConfigurationRepository = notificationConfigurationRepository;
        this.emailConfigRepository = emailConfigRepository;
        this.sMSConfigRepository = sMSConfigRepository;
        this.inAppConfigRepository = inAppConfigRepository;
        this.whatsAppConfigRepository = whatsAppConfigRepository;
    }

    public List<NotificationConfigurationTypeDTO> findAll() {
        final List<NotificationConfigurationType> notificationConfigurationTypes = notificationConfigurationTypeRepository.findAll(Sort.by("configTypeID"));
        return notificationConfigurationTypes.stream()
                .map(notificationConfigurationType -> mapToDTO(notificationConfigurationType, new NotificationConfigurationTypeDTO()))
                .toList();
    }

    public NotificationConfigurationTypeDTO get(final UUID configTypeID) {
        return notificationConfigurationTypeRepository.findById(configTypeID)
                .map(notificationConfigurationType -> mapToDTO(notificationConfigurationType, new NotificationConfigurationTypeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final NotificationConfigurationTypeDTO notificationConfigurationTypeDTO) {
        final NotificationConfigurationType notificationConfigurationType = new NotificationConfigurationType();
        mapToEntity(notificationConfigurationTypeDTO, notificationConfigurationType);
        return notificationConfigurationTypeRepository.save(notificationConfigurationType).getConfigTypeID();
    }

    public void update(final UUID configTypeID,
            final NotificationConfigurationTypeDTO notificationConfigurationTypeDTO) {
        final NotificationConfigurationType notificationConfigurationType = notificationConfigurationTypeRepository.findById(configTypeID)
                .orElseThrow(NotFoundException::new);
        mapToEntity(notificationConfigurationTypeDTO, notificationConfigurationType);
        notificationConfigurationTypeRepository.save(notificationConfigurationType);
    }

    public void delete(final UUID configTypeID) {
        notificationConfigurationTypeRepository.deleteById(configTypeID);
    }

    private NotificationConfigurationTypeDTO mapToDTO(
            final NotificationConfigurationType notificationConfigurationType,
            final NotificationConfigurationTypeDTO notificationConfigurationTypeDTO) {
        notificationConfigurationTypeDTO.setConfigTypeID(notificationConfigurationType.getConfigTypeID());
        notificationConfigurationTypeDTO.setNotificationType(notificationConfigurationType.getNotificationType());
        notificationConfigurationTypeDTO.setDeleteAt(notificationConfigurationType.getDeleteAt());
        notificationConfigurationTypeDTO.setConfigId(notificationConfigurationType.getConfigId() == null ? null : notificationConfigurationType.getConfigId().getConfigID());
        return notificationConfigurationTypeDTO;
    }

    private NotificationConfigurationType mapToEntity(
            final NotificationConfigurationTypeDTO notificationConfigurationTypeDTO,
            final NotificationConfigurationType notificationConfigurationType) {
        notificationConfigurationType.setNotificationType(notificationConfigurationTypeDTO.getNotificationType());
        notificationConfigurationType.setDeleteAt(notificationConfigurationTypeDTO.getDeleteAt());
        final NotificationConfiguration configId = notificationConfigurationTypeDTO.getConfigId() == null ? null : notificationConfigurationRepository.findById(notificationConfigurationTypeDTO.getConfigId())
                .orElseThrow(() -> new NotFoundException("configId not found"));
        notificationConfigurationType.setConfigId(configId);
        return notificationConfigurationType;
    }

    public ReferencedWarning getReferencedWarning(final UUID configTypeID) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final NotificationConfigurationType notificationConfigurationType = notificationConfigurationTypeRepository.findById(configTypeID)
                .orElseThrow(NotFoundException::new);
        final EmailConfig notificationTypeIDEmailConfig = emailConfigRepository.findFirstByNotificationTypeID(notificationConfigurationType);
        if (notificationTypeIDEmailConfig != null) {
            referencedWarning.setKey("notificationConfigurationType.emailConfig.notificationTypeID.referenced");
            referencedWarning.addParam(notificationTypeIDEmailConfig.getConfigId());
            return referencedWarning;
        }
        final SMSConfig notificationTypeIDSMSConfig = sMSConfigRepository.findFirstByNotificationTypeID(notificationConfigurationType);
        if (notificationTypeIDSMSConfig != null) {
            referencedWarning.setKey("notificationConfigurationType.sMSConfig.notificationTypeID.referenced");
            referencedWarning.addParam(notificationTypeIDSMSConfig.getConfigId());
            return referencedWarning;
        }
        final InAppConfig notificationTypeIDInAppConfig = inAppConfigRepository.findFirstByNotificationTypeID(notificationConfigurationType);
        if (notificationTypeIDInAppConfig != null) {
            referencedWarning.setKey("notificationConfigurationType.inAppConfig.notificationTypeID.referenced");
            referencedWarning.addParam(notificationTypeIDInAppConfig.getConfigID());
            return referencedWarning;
        }
        final WhatsAppConfig notificationTypeIDWhatsAppConfig = whatsAppConfigRepository.findFirstByNotificationTypeID(notificationConfigurationType);
        if (notificationTypeIDWhatsAppConfig != null) {
            referencedWarning.setKey("notificationConfigurationType.whatsAppConfig.notificationTypeID.referenced");
            referencedWarning.addParam(notificationTypeIDWhatsAppConfig.getConfigID());
            return referencedWarning;
        }
        return null;
    }

}
