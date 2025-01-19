package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Template;
import io.ankush.kap_mini.model.TemplateDTO;
import io.ankush.kap_mini.repos.TemplateRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateService(final TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public List<TemplateDTO> findAll() {
        final List<Template> templates = templateRepository.findAll(Sort.by("templateId"));
        return templates.stream()
                .map(template -> mapToDTO(template, new TemplateDTO()))
                .toList();
    }

    public TemplateDTO get(final UUID templateId) {
        return templateRepository.findById(templateId)
                .map(template -> mapToDTO(template, new TemplateDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final TemplateDTO templateDTO) {
        final Template template = new Template();
        mapToEntity(templateDTO, template);
        return templateRepository.save(template).getTemplateId();
    }

    public void update(final UUID templateId, final TemplateDTO templateDTO) {
        final Template template = templateRepository.findById(templateId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(templateDTO, template);
        templateRepository.save(template);
    }

    public void delete(final UUID templateId) {
        templateRepository.deleteById(templateId);
    }

    private TemplateDTO mapToDTO(final Template template, final TemplateDTO templateDTO) {
        templateDTO.setTemplateId(template.getTemplateId());
        templateDTO.setTemplateName(template.getTemplateName());
        templateDTO.setDescription(template.getDescription());
        templateDTO.setTemplateData(template.getTemplateData());
        return templateDTO;
    }

    private Template mapToEntity(final TemplateDTO templateDTO, final Template template) {
        template.setTemplateName(templateDTO.getTemplateName());
        template.setDescription(templateDTO.getDescription());
        template.setTemplateData(templateDTO.getTemplateData());
        return template;
    }

}
