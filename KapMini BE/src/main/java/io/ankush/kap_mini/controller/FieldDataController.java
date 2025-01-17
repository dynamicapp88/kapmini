package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.Field;
import io.ankush.kap_mini.domain.Record;
import io.ankush.kap_mini.model.FieldDataDTO;
import io.ankush.kap_mini.repos.FieldRepository;
import io.ankush.kap_mini.repos.RecordRepository;
import io.ankush.kap_mini.service.FieldDataService;
import io.ankush.kap_mini.util.CustomCollectors;
import io.ankush.kap_mini.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/fieldDatas")
public class FieldDataController {

    private final FieldDataService fieldDataService;
    private final FieldRepository fieldRepository;
    private final RecordRepository recordRepository;

    public FieldDataController(final FieldDataService fieldDataService,
            final FieldRepository fieldRepository, final RecordRepository recordRepository) {
        this.fieldDataService = fieldDataService;
        this.fieldRepository = fieldRepository;
        this.recordRepository = recordRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("fieldIdValues", fieldRepository.findAll(Sort.by("fieldId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Field::getFieldId, Field::getFieldName)));
        model.addAttribute("recordIdValues", recordRepository.findAll(Sort.by("recordId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Record::getRecordId, Record::getStatus)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("fieldDatas", fieldDataService.findAll());
        return "fieldData/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("fieldData") final FieldDataDTO fieldDataDTO) {
        return "fieldData/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("fieldData") @Valid final FieldDataDTO fieldDataDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "fieldData/add";
        }
        fieldDataService.create(fieldDataDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("fieldData.create.success"));
        return "redirect:/fieldDatas";
    }

    @GetMapping("/edit/{fieldDataId}")
    public String edit(@PathVariable(name = "fieldDataId") final UUID fieldDataId,
            final Model model) {
        model.addAttribute("fieldData", fieldDataService.get(fieldDataId));
        return "fieldData/edit";
    }

    @PostMapping("/edit/{fieldDataId}")
    public String edit(@PathVariable(name = "fieldDataId") final UUID fieldDataId,
            @ModelAttribute("fieldData") @Valid final FieldDataDTO fieldDataDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "fieldData/edit";
        }
        fieldDataService.update(fieldDataId, fieldDataDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("fieldData.update.success"));
        return "redirect:/fieldDatas";
    }

    @PostMapping("/delete/{fieldDataId}")
    public String delete(@PathVariable(name = "fieldDataId") final UUID fieldDataId,
            final RedirectAttributes redirectAttributes) {
        fieldDataService.delete(fieldDataId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("fieldData.delete.success"));
        return "redirect:/fieldDatas";
    }

}
