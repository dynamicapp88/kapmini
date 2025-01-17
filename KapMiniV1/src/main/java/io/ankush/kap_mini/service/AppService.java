package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.History;
import io.ankush.kap_mini.domain.SubDomain;
import io.ankush.kap_mini.domain.Workflow;
import io.ankush.kap_mini.model.AppDTO;
import io.ankush.kap_mini.repos.AppRepository;
import io.ankush.kap_mini.repos.FormRepository;
import io.ankush.kap_mini.repos.HistoryRepository;
import io.ankush.kap_mini.repos.SubDomainRepository;
import io.ankush.kap_mini.repos.WorkflowRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AppService {

    private final AppRepository appRepository;
    private final SubDomainRepository subDomainRepository;
    private final FormRepository formRepository;
    private final WorkflowRepository workflowRepository;
    private final HistoryRepository historyRepository;

    public AppService(final AppRepository appRepository,
            final SubDomainRepository subDomainRepository, final FormRepository formRepository,
            final WorkflowRepository workflowRepository,
            final HistoryRepository historyRepository) {
        this.appRepository = appRepository;
        this.subDomainRepository = subDomainRepository;
        this.formRepository = formRepository;
        this.workflowRepository = workflowRepository;
        this.historyRepository = historyRepository;
    }

    public List<AppDTO> findAll() {
        final List<App> apps = appRepository.findAll(Sort.by("appId"));
        return apps.stream()
                .map(app -> mapToDTO(app, new AppDTO()))
                .toList();
    }

    public AppDTO get(final UUID appId) {
        return appRepository.findById(appId)
                .map(app -> mapToDTO(app, new AppDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final AppDTO appDTO) {
        final App app = new App();
        mapToEntity(appDTO, app);
        return appRepository.save(app).getAppId();
    }

    public void update(final UUID appId, final AppDTO appDTO) {
        final App app = appRepository.findById(appId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(appDTO, app);
        appRepository.save(app);
    }

    public void delete(final UUID appId) {
        appRepository.deleteById(appId);
    }

    private AppDTO mapToDTO(final App app, final AppDTO appDTO) {
        appDTO.setAppId(app.getAppId());
        appDTO.setName(app.getName());
        appDTO.setDescription(app.getDescription());
        appDTO.setIsActive(app.getIsActive());
        appDTO.setIsProduction(app.getIsProduction());
        appDTO.setDeleteAt(app.getDeleteAt());
        appDTO.setSubdomainId(app.getSubdomainId() == null ? null : app.getSubdomainId().getSubdomain());
        return appDTO;
    }

    private App mapToEntity(final AppDTO appDTO, final App app) {
        app.setName(appDTO.getName());
        app.setDescription(appDTO.getDescription());
        app.setIsActive(appDTO.getIsActive());
        app.setIsProduction(appDTO.getIsProduction());
        app.setDeleteAt(appDTO.getDeleteAt());
        final SubDomain subdomainId = appDTO.getSubdomainId() == null ? null : subDomainRepository.findById(appDTO.getSubdomainId())
                .orElseThrow(() -> new NotFoundException("subdomainId not found"));
        app.setSubdomainId(subdomainId);
        return app;
    }

    public ReferencedWarning getReferencedWarning(final UUID appId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final App app = appRepository.findById(appId)
                .orElseThrow(NotFoundException::new);
        final Form appIDForm = formRepository.findFirstByAppID(app);
        if (appIDForm != null) {
            referencedWarning.setKey("app.form.appID.referenced");
            referencedWarning.addParam(appIDForm.getFormId());
            return referencedWarning;
        }
        final Workflow appIdWorkflow = workflowRepository.findFirstByAppId(app);
        if (appIdWorkflow != null) {
            referencedWarning.setKey("app.workflow.appId.referenced");
            referencedWarning.addParam(appIdWorkflow.getWorkflowId());
            return referencedWarning;
        }
        final History appIDHistory = historyRepository.findFirstByAppID(app);
        if (appIDHistory != null) {
            referencedWarning.setKey("app.history.appID.referenced");
            referencedWarning.addParam(appIDHistory.getHistoryID());
            return referencedWarning;
        }
        return null;
    }

}
