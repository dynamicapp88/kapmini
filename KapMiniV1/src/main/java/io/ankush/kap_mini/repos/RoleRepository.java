package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Role;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, UUID> {
}
