package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.domain.WhatsAppConfig;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WhatsAppConfigRepository extends JpaRepository<WhatsAppConfig, Long> {

    WhatsAppConfig findFirstByNotificationTypeID(
            NotificationConfigurationType notificationConfigurationType);

}
