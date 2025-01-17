package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Role;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.domain.UserRole;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    UserRole findFirstByUserId(User user);

    UserRole findFirstByRoleId(Role role);

}
