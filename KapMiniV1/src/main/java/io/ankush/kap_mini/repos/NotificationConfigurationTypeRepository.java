package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.NotificationConfiguration;
import io.ankush.kap_mini.domain.NotificationConfigurationType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationConfigurationTypeRepository extends JpaRepository<NotificationConfigurationType, UUID> {

    NotificationConfigurationType findFirstByConfigId(
            NotificationConfiguration notificationConfiguration);

}
