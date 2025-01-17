package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.History;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.HistoryDTO;
import io.ankush.kap_mini.repos.AppRepository;
import io.ankush.kap_mini.repos.FormRepository;
import io.ankush.kap_mini.repos.HistoryRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final AppRepository appRepository;
    private final FormRepository formRepository;

    public HistoryService(final HistoryRepository historyRepository,
            final UserRepository userRepository, final AppRepository appRepository,
            final FormRepository formRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.appRepository = appRepository;
        this.formRepository = formRepository;
    }

    public List<HistoryDTO> findAll() {
        final List<History> histories = historyRepository.findAll(Sort.by("historyID"));
        return histories.stream()
                .map(history -> mapToDTO(history, new HistoryDTO()))
                .toList();
    }

    public HistoryDTO get(final UUID historyID) {
        return historyRepository.findById(historyID)
                .map(history -> mapToDTO(history, new HistoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final HistoryDTO historyDTO) {
        final History history = new History();
        mapToEntity(historyDTO, history);
        return historyRepository.save(history).getHistoryID();
    }

    public void update(final UUID historyID, final HistoryDTO historyDTO) {
        final History history = historyRepository.findById(historyID)
                .orElseThrow(NotFoundException::new);
        mapToEntity(historyDTO, history);
        historyRepository.save(history);
    }

    public void delete(final UUID historyID) {
        historyRepository.deleteById(historyID);
    }

    private HistoryDTO mapToDTO(final History history, final HistoryDTO historyDTO) {
        historyDTO.setHistoryID(history.getHistoryID());
        historyDTO.setTableName(history.getTableName());
        historyDTO.setActionType(history.getActionType());
        historyDTO.setActionDetails(history.getActionDetails());
        historyDTO.setPerformedBy(history.getPerformedBy());
        historyDTO.setDeleteAt(history.getDeleteAt());
        historyDTO.setUserId(history.getUserId() == null ? null : history.getUserId().getUserId());
        historyDTO.setAppID(history.getAppID() == null ? null : history.getAppID().getAppId());
        historyDTO.setFormId(history.getFormId() == null ? null : history.getFormId().getFormId());
        return historyDTO;
    }

    private History mapToEntity(final HistoryDTO historyDTO, final History history) {
        history.setTableName(historyDTO.getTableName());
        history.setActionType(historyDTO.getActionType());
        history.setActionDetails(historyDTO.getActionDetails());
        history.setPerformedBy(historyDTO.getPerformedBy());
        history.setDeleteAt(historyDTO.getDeleteAt());
        final User userId = historyDTO.getUserId() == null ? null : userRepository.findById(historyDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        history.setUserId(userId);
        final App appID = historyDTO.getAppID() == null ? null : appRepository.findById(historyDTO.getAppID())
                .orElseThrow(() -> new NotFoundException("appID not found"));
        history.setAppID(appID);
        final Form formId = historyDTO.getFormId() == null ? null : formRepository.findById(historyDTO.getFormId())
                .orElseThrow(() -> new NotFoundException("formId not found"));
        history.setFormId(formId);
        return history;
    }

}
