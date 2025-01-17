package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.EmailConfig;
import io.ankush.kap_mini.domain.NotificationConfigurationType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmailConfigRepository extends JpaRepository<EmailConfig, UUID> {

    EmailConfig findFirstByNotificationTypeID(
            NotificationConfigurationType notificationConfigurationType);

}
