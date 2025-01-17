package io.ankush.kap_mini.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.ankush.kap_mini.service.WorkflowStepService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the stepID value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = WorkflowStepPrevWorkFlowStepIdUnique.WorkflowStepPrevWorkFlowStepIdUniqueValidator.class
)
public @interface WorkflowStepPrevWorkFlowStepIdUnique {

    String message() default "{Exists.workflowStep.prevWorkFlowStepId}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class WorkflowStepPrevWorkFlowStepIdUniqueValidator implements ConstraintValidator<WorkflowStepPrevWorkFlowStepIdUnique, UUID> {

        private final WorkflowStepService workflowStepService;
        private final HttpServletRequest request;

        public WorkflowStepPrevWorkFlowStepIdUniqueValidator(
                final WorkflowStepService workflowStepService, final HttpServletRequest request) {
            this.workflowStepService = workflowStepService;
            this.request = request;
        }

        @Override
        public boolean isValid(final UUID value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("stepID");
            if (currentId != null && value.equals(workflowStepService.get(UUID.fromString(currentId)).getPrevWorkFlowStepId())) {
                // value hasn't changed
                return true;
            }
            return !workflowStepService.prevWorkFlowStepIdExists(value);
        }

    }

}
