package io.ankush.kap_mini.domain;

import io.ankush.kap_mini.model.ActionType;
import io.ankush.kap_mini.model.StepType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class WorkflowStep {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID stepID;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StepType stepType;

    @Column(name = "\"condition\"")
    private String condition;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column
    private String actionDetails;

    @Column
    private LocalDateTime deleteAt;

    @Column
    private Integer sequenceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id_id", nullable = false)
    private Workflow workflowId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id_id")
    private Form formId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id_id")
    private Field fieldId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_work_flow_step_id_id", unique = true)
    private WorkflowStep nextWorkFlowStepId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_work_flow_step_id_id", unique = true)
    private WorkflowStep prevWorkFlowStepId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
