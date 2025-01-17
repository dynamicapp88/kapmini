package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.Workflow;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WorkflowRepository extends JpaRepository<Workflow, UUID> {

    Workflow findFirstByAppId(App app);

}
