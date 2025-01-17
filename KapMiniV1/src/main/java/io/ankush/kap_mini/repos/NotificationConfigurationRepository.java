package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.NotificationConfiguration;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationConfigurationRepository extends JpaRepository<NotificationConfiguration, UUID> {
}
