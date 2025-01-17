package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.model.SMSConfigDTO;
import io.ankush.kap_mini.repos.NotificationConfigurationTypeRepository;
import io.ankush.kap_mini.service.SMSConfigService;
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
@RequestMapping("/sMSConfigs")
public class SMSConfigController {

    private final SMSConfigService sMSConfigService;
    private final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository;

    public SMSConfigController(final SMSConfigService sMSConfigService,
            final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository) {
        this.sMSConfigService = sMSConfigService;
        this.notificationConfigurationTypeRepository = notificationConfigurationTypeRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("notificationTypeIDValues", notificationConfigurationTypeRepository.findAll(Sort.by("configTypeID"))
                .stream()
                .collect(CustomCollectors.toSortedMap(NotificationConfigurationType::getConfigTypeID, NotificationConfigurationType::getConfigTypeID)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("sMSConfigs", sMSConfigService.findAll());
        return "sMSConfig/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("sMSConfig") final SMSConfigDTO sMSConfigDTO) {
        return "sMSConfig/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("sMSConfig") @Valid final SMSConfigDTO sMSConfigDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "sMSConfig/add";
        }
        sMSConfigService.create(sMSConfigDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("sMSConfig.create.success"));
        return "redirect:/sMSConfigs";
    }

    @GetMapping("/edit/{configId}")
    public String edit(@PathVariable(name = "configId") final UUID configId, final Model model) {
        model.addAttribute("sMSConfig", sMSConfigService.get(configId));
        return "sMSConfig/edit";
    }

    @PostMapping("/edit/{configId}")
    public String edit(@PathVariable(name = "configId") final UUID configId,
            @ModelAttribute("sMSConfig") @Valid final SMSConfigDTO sMSConfigDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "sMSConfig/edit";
        }
        sMSConfigService.update(configId, sMSConfigDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("sMSConfig.update.success"));
        return "redirect:/sMSConfigs";
    }

    @PostMapping("/delete/{configId}")
    public String delete(@PathVariable(name = "configId") final UUID configId,
            final RedirectAttributes redirectAttributes) {
        sMSConfigService.delete(configId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("sMSConfig.delete.success"));
        return "redirect:/sMSConfigs";
    }

}
