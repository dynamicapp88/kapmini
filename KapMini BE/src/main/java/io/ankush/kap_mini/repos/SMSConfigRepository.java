package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.domain.SMSConfig;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SMSConfigRepository extends JpaRepository<SMSConfig, UUID> {

    SMSConfig findFirstByNotificationTypeID(
            NotificationConfigurationType notificationConfigurationType);

}
