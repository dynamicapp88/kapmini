package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.domain.UserSetting;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserSettingRepository extends JpaRepository<UserSetting, UUID> {

    UserSetting findFirstByUserId(User user);

}
