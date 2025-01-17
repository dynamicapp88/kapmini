package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.History;
import io.ankush.kap_mini.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HistoryRepository extends JpaRepository<History, UUID> {

    History findFirstByUserId(User user);

    History findFirstByAppID(App app);

    History findFirstByFormId(Form form);

}
