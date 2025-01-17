package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Field;
import io.ankush.kap_mini.domain.FieldData;
import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.model.FieldDTO;
import io.ankush.kap_mini.repos.FieldDataRepository;
import io.ankush.kap_mini.repos.FieldRepository;
import io.ankush.kap_mini.repos.FormRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class FieldService {

    private final FieldRepository fieldRepository;
    private final FormRepository formRepository;
    private final FieldDataRepository fieldDataRepository;

    public FieldService(final FieldRepository fieldRepository, final FormRepository formRepository,
            final FieldDataRepository fieldDataRepository) {
        this.fieldRepository = fieldRepository;
        this.formRepository = formRepository;
        this.fieldDataRepository = fieldDataRepository;
    }

    public List<FieldDTO> findAll() {
        final List<Field> fields = fieldRepository.findAll(Sort.by("fieldId"));
        return fields.stream()
                .map(field -> mapToDTO(field, new FieldDTO()))
                .toList();
    }

    public FieldDTO get(final UUID fieldId) {
        return fieldRepository.findById(fieldId)
                .map(field -> mapToDTO(field, new FieldDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final FieldDTO fieldDTO) {
        final Field field = new Field();
        mapToEntity(fieldDTO, field);
        return fieldRepository.save(field).getFieldId();
    }

    public void update(final UUID fieldId, final FieldDTO fieldDTO) {
        final Field field = fieldRepository.findById(fieldId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(fieldDTO, field);
        fieldRepository.save(field);
    }

    public void delete(final UUID fieldId) {
        fieldRepository.deleteById(fieldId);
    }

    private FieldDTO mapToDTO(final Field field, final FieldDTO fieldDTO) {
        fieldDTO.setFieldId(field.getFieldId());
        fieldDTO.setFieldName(field.getFieldName());
        fieldDTO.setFieldType(field.getFieldType());
        fieldDTO.setDefaultValue(field.getDefaultValue());
        fieldDTO.setValidationRules(field.getValidationRules());
        fieldDTO.setIsRequired(field.getIsRequired());
        fieldDTO.setDeleteAt(field.getDeleteAt());
        fieldDTO.setFormId(field.getFormId() == null ? null : field.getFormId().getFormId());
        return fieldDTO;
    }

    private Field mapToEntity(final FieldDTO fieldDTO, final Field field) {
        field.setFieldName(fieldDTO.getFieldName());
        field.setFieldType(fieldDTO.getFieldType());
        field.setDefaultValue(fieldDTO.getDefaultValue());
        field.setValidationRules(fieldDTO.getValidationRules());
        field.setIsRequired(fieldDTO.getIsRequired());
        field.setDeleteAt(fieldDTO.getDeleteAt());
        final Form formId = fieldDTO.getFormId() == null ? null : formRepository.findById(fieldDTO.getFormId())
                .orElseThrow(() -> new NotFoundException("formId not found"));
        field.setFormId(formId);
        return field;
    }

    public ReferencedWarning getReferencedWarning(final UUID fieldId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Field field = fieldRepository.findById(fieldId)
                .orElseThrow(NotFoundException::new);
        final FieldData fieldIdFieldData = fieldDataRepository.findFirstByFieldId(field);
        if (fieldIdFieldData != null) {
            referencedWarning.setKey("field.fieldData.fieldId.referenced");
            referencedWarning.addParam(fieldIdFieldData.getFieldDataId());
            return referencedWarning;
        }
        return null;
    }

}
