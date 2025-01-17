package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.Form;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FormRepository extends JpaRepository<Form, UUID> {

    Form findFirstByAppID(App app);

}
