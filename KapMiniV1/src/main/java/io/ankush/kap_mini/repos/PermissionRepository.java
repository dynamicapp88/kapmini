package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Permission;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissionRepository extends JpaRepository<Permission, UUID> {
}
