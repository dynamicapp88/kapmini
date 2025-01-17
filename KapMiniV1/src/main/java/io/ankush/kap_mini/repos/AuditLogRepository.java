package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.AuditLog;
import io.ankush.kap_mini.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    AuditLog findFirstByUserId(User user);

}
