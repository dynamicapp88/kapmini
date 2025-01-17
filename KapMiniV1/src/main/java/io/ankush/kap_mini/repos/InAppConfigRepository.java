package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.InAppConfig;
import io.ankush.kap_mini.domain.NotificationConfigurationType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InAppConfigRepository extends JpaRepository<InAppConfig, UUID> {

    InAppConfig findFirstByNotificationTypeID(
            NotificationConfigurationType notificationConfigurationType);

}
