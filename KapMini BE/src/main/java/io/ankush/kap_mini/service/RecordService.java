package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.FieldData;
import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.Record;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.RecordDTO;
import io.ankush.kap_mini.repos.FieldDataRepository;
import io.ankush.kap_mini.repos.FormRepository;
import io.ankush.kap_mini.repos.RecordRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RecordService {

    private final RecordRepository recordRepository;
    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final FieldDataRepository fieldDataRepository;

    public RecordService(final RecordRepository recordRepository,
            final FormRepository formRepository, final UserRepository userRepository,
            final FieldDataRepository fieldDataRepository) {
        this.recordRepository = recordRepository;
        this.formRepository = formRepository;
        this.userRepository = userRepository;
        this.fieldDataRepository = fieldDataRepository;
    }

    public List<RecordDTO> findAll() {
        final List<Record> records = recordRepository.findAll(Sort.by("recordId"));
        return records.stream()
                .map(record -> mapToDTO(record, new RecordDTO()))
                .toList();
    }

    public RecordDTO get(final UUID recordId) {
        return recordRepository.findById(recordId)
                .map(record -> mapToDTO(record, new RecordDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final RecordDTO recordDTO) {
        final Record record = new Record();
        mapToEntity(recordDTO, record);
        return recordRepository.save(record).getRecordId();
    }

    public void update(final UUID recordId, final RecordDTO recordDTO) {
        final Record record = recordRepository.findById(recordId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(recordDTO, record);
        recordRepository.save(record);
    }

    public void delete(final UUID recordId) {
        recordRepository.deleteById(recordId);
    }

    private RecordDTO mapToDTO(final Record record, final RecordDTO recordDTO) {
        recordDTO.setRecordId(record.getRecordId());
        recordDTO.setStatus(record.getStatus());
        recordDTO.setFormId(record.getFormId() == null ? null : record.getFormId().getFormId());
        recordDTO.setCreatedBy(record.getCreatedBy() == null ? null : record.getCreatedBy().getUserId());
        recordDTO.setUpdatedBy(record.getUpdatedBy() == null ? null : record.getUpdatedBy().getUserId());
        return recordDTO;
    }

    private Record mapToEntity(final RecordDTO recordDTO, final Record record) {
        record.setStatus(recordDTO.getStatus());
        final Form formId = recordDTO.getFormId() == null ? null : formRepository.findById(recordDTO.getFormId())
                .orElseThrow(() -> new NotFoundException("formId not found"));
        record.setFormId(formId);
        final User createdBy = recordDTO.getCreatedBy() == null ? null : userRepository.findById(recordDTO.getCreatedBy())
                .orElseThrow(() -> new NotFoundException("createdBy not found"));
        record.setCreatedBy(createdBy);
        final User updatedBy = recordDTO.getUpdatedBy() == null ? null : userRepository.findById(recordDTO.getUpdatedBy())
                .orElseThrow(() -> new NotFoundException("updatedBy not found"));
        record.setUpdatedBy(updatedBy);
        return record;
    }

    public ReferencedWarning getReferencedWarning(final UUID recordId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Record record = recordRepository.findById(recordId)
                .orElseThrow(NotFoundException::new);
        final FieldData recordIdFieldData = fieldDataRepository.findFirstByRecordId(record);
        if (recordIdFieldData != null) {
            referencedWarning.setKey("record.fieldData.recordId.referenced");
            referencedWarning.addParam(recordIdFieldData.getFieldDataId());
            return referencedWarning;
        }
        return null;
    }

}
