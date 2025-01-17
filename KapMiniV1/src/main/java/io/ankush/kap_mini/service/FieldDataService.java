package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Field;
import io.ankush.kap_mini.domain.FieldData;
import io.ankush.kap_mini.domain.Record;
import io.ankush.kap_mini.model.FieldDataDTO;
import io.ankush.kap_mini.repos.FieldDataRepository;
import io.ankush.kap_mini.repos.FieldRepository;
import io.ankush.kap_mini.repos.RecordRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class FieldDataService {

    private final FieldDataRepository fieldDataRepository;
    private final FieldRepository fieldRepository;
    private final RecordRepository recordRepository;

    public FieldDataService(final FieldDataRepository fieldDataRepository,
            final FieldRepository fieldRepository, final RecordRepository recordRepository) {
        this.fieldDataRepository = fieldDataRepository;
        this.fieldRepository = fieldRepository;
        this.recordRepository = recordRepository;
    }

    public List<FieldDataDTO> findAll() {
        final List<FieldData> fieldDatas = fieldDataRepository.findAll(Sort.by("fieldDataId"));
        return fieldDatas.stream()
                .map(fieldData -> mapToDTO(fieldData, new FieldDataDTO()))
                .toList();
    }

    public FieldDataDTO get(final UUID fieldDataId) {
        return fieldDataRepository.findById(fieldDataId)
                .map(fieldData -> mapToDTO(fieldData, new FieldDataDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final FieldDataDTO fieldDataDTO) {
        final FieldData fieldData = new FieldData();
        mapToEntity(fieldDataDTO, fieldData);
        return fieldDataRepository.save(fieldData).getFieldDataId();
    }

    public void update(final UUID fieldDataId, final FieldDataDTO fieldDataDTO) {
        final FieldData fieldData = fieldDataRepository.findById(fieldDataId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(fieldDataDTO, fieldData);
        fieldDataRepository.save(fieldData);
    }

    public void delete(final UUID fieldDataId) {
        fieldDataRepository.deleteById(fieldDataId);
    }

    private FieldDataDTO mapToDTO(final FieldData fieldData, final FieldDataDTO fieldDataDTO) {
        fieldDataDTO.setFieldDataId(fieldData.getFieldDataId());
        fieldDataDTO.setValue(fieldData.getValue());
        fieldDataDTO.setDeleteAt(fieldData.getDeleteAt());
        fieldDataDTO.setFieldId(fieldData.getFieldId() == null ? null : fieldData.getFieldId().getFieldId());
        fieldDataDTO.setRecordId(fieldData.getRecordId() == null ? null : fieldData.getRecordId().getRecordId());
        return fieldDataDTO;
    }

    private FieldData mapToEntity(final FieldDataDTO fieldDataDTO, final FieldData fieldData) {
        fieldData.setValue(fieldDataDTO.getValue());
        fieldData.setDeleteAt(fieldDataDTO.getDeleteAt());
        final Field fieldId = fieldDataDTO.getFieldId() == null ? null : fieldRepository.findById(fieldDataDTO.getFieldId())
                .orElseThrow(() -> new NotFoundException("fieldId not found"));
        fieldData.setFieldId(fieldId);
        final Record recordId = fieldDataDTO.getRecordId() == null ? null : recordRepository.findById(fieldDataDTO.getRecordId())
                .orElseThrow(() -> new NotFoundException("recordId not found"));
        fieldData.setRecordId(recordId);
        return fieldData;
    }

}
