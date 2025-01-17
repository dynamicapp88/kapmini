package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Permission;
import io.ankush.kap_mini.domain.RolePermission;
import io.ankush.kap_mini.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

    RolePermission findFirstByRoleId(User user);

    RolePermission findFirstByPremissionId(Permission permission);

}
