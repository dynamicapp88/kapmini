package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Field;
import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.Workflow;
import io.ankush.kap_mini.domain.WorkflowStep;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, UUID> {

    WorkflowStep findFirstByWorkflowId(Workflow workflow);

    WorkflowStep findFirstByFormId(Form form);

    WorkflowStep findFirstByFieldId(Field field);

    WorkflowStep findFirstByNextWorkFlowStepIdAndStepIDNot(WorkflowStep workflowStep,
            final UUID stepID);

    WorkflowStep findFirstByPrevWorkFlowStepIdAndStepIDNot(WorkflowStep workflowStep,
            final UUID stepID);

    boolean existsByNextWorkFlowStepIdStepID(UUID stepID);

    boolean existsByPrevWorkFlowStepIdStepID(UUID stepID);

}
