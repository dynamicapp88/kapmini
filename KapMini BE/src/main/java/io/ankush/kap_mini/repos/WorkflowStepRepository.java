package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Workflow;
import io.ankush.kap_mini.domain.WorkflowStep;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, UUID> {

    WorkflowStep findFirstByWorkflowId(Workflow workflow);

}
