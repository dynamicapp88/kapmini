package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.Field;
import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.History;
import io.ankush.kap_mini.domain.Record;
import io.ankush.kap_mini.domain.WorkflowStep;
import io.ankush.kap_mini.model.FormDTO;
import io.ankush.kap_mini.repos.AppRepository;
import io.ankush.kap_mini.repos.FieldRepository;
import io.ankush.kap_mini.repos.FormRepository;
import io.ankush.kap_mini.repos.HistoryRepository;
import io.ankush.kap_mini.repos.RecordRepository;
import io.ankush.kap_mini.repos.WorkflowStepRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class FormService {

    private final FormRepository formRepository;
    private final AppRepository appRepository;
    private final FieldRepository fieldRepository;
    private final RecordRepository recordRepository;
    private final HistoryRepository historyRepository;
    private final WorkflowStepRepository workflowStepRepository;

    public FormService(final FormRepository formRepository, final AppRepository appRepository,
            final FieldRepository fieldRepository, final RecordRepository recordRepository,
            final HistoryRepository historyRepository,
            final WorkflowStepRepository workflowStepRepository) {
        this.formRepository = formRepository;
        this.appRepository = appRepository;
        this.fieldRepository = fieldRepository;
        this.recordRepository = recordRepository;
        this.historyRepository = historyRepository;
        this.workflowStepRepository = workflowStepRepository;
    }

    public List<FormDTO> findAll() {
        final List<Form> forms = formRepository.findAll(Sort.by("formId"));
        return forms.stream()
                .map(form -> mapToDTO(form, new FormDTO()))
                .toList();
    }

    public List<FormDTO> findAllBasedOnAppId(final UUID appId) {
        final List<Form> forms = formRepository.findAll(Sort.by("formId"));
         List<Form> reF = new ArrayList<>();
         for(Form f : forms){
             App a = f.getAppID();
             if(a!= null && a.getAppId().equals(appId)){
                 reF.add(f);
             }
         }
        return reF.stream()
                .map(form -> mapToDTO(form, new FormDTO()))
                .toList();
    }

    public FormDTO get(final UUID formId) {
        return formRepository.findById(formId)
                .map(form -> mapToDTO(form, new FormDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final FormDTO formDTO) {
        final Form form = new Form();
        mapToEntity(formDTO, form);
        return formRepository.save(form).getFormId();
    }

    public void update(final UUID formId, final FormDTO formDTO) {
        final Form form = formRepository.findById(formId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(formDTO, form);
        formRepository.save(form);
    }

    public void delete(final UUID formId) {
        formRepository.deleteById(formId);
    }

    private FormDTO mapToDTO(final Form form, final FormDTO formDTO) {
        formDTO.setFormId(form.getFormId());
        formDTO.setName(form.getName());
        formDTO.setDescription(form.getDescription());
        formDTO.setDeleteAt(form.getDeleteAt());
        formDTO.setAppID(form.getAppID() == null ? null : form.getAppID().getAppId());
        return formDTO;
    }

    private Form mapToEntity(final FormDTO formDTO, final Form form) {
        form.setName(formDTO.getName());
        form.setDescription(formDTO.getDescription());
        form.setDeleteAt(formDTO.getDeleteAt());
        final App appID = formDTO.getAppID() == null ? null : appRepository.findById(formDTO.getAppID())
                .orElseThrow(() -> new NotFoundException("appID not found"));
        form.setAppID(appID);
        return form;
    }

    public ReferencedWarning getReferencedWarning(final UUID formId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Form form = formRepository.findById(formId)
                .orElseThrow(NotFoundException::new);
        final Field formIdField = fieldRepository.findFirstByFormId(form);
        if (formIdField != null) {
            referencedWarning.setKey("form.field.formId.referenced");
            referencedWarning.addParam(formIdField.getFieldId());
            return referencedWarning;
        }
        final Record formIdRecord = recordRepository.findFirstByFormId(form);
        if (formIdRecord != null) {
            referencedWarning.setKey("form.record.formId.referenced");
            referencedWarning.addParam(formIdRecord.getRecordId());
            return referencedWarning;
        }
        final History formIdHistory = historyRepository.findFirstByFormId(form);
        if (formIdHistory != null) {
            referencedWarning.setKey("form.history.formId.referenced");
            referencedWarning.addParam(formIdHistory.getHistoryID());
            return referencedWarning;
        }
        final WorkflowStep formIdWorkflowStep = workflowStepRepository.findFirstByFormId(form);
        if (formIdWorkflowStep != null) {
            referencedWarning.setKey("form.workflowStep.formId.referenced");
            referencedWarning.addParam(formIdWorkflowStep.getStepID());
            return referencedWarning;
        }
        return null;
    }

}
