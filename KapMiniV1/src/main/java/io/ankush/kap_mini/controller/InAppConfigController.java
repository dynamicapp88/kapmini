package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.model.InAppConfigDTO;
import io.ankush.kap_mini.repos.NotificationConfigurationTypeRepository;
import io.ankush.kap_mini.service.InAppConfigService;
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
@RequestMapping("/inAppConfigs")
public class InAppConfigController {

    private final InAppConfigService inAppConfigService;
    private final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository;

    public InAppConfigController(final InAppConfigService inAppConfigService,
            final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository) {
        this.inAppConfigService = inAppConfigService;
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
        model.addAttribute("inAppConfigs", inAppConfigService.findAll());
        return "inAppConfig/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("inAppConfig") final InAppConfigDTO inAppConfigDTO) {
        return "inAppConfig/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("inAppConfig") @Valid final InAppConfigDTO inAppConfigDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inAppConfig/add";
        }
        inAppConfigService.create(inAppConfigDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("inAppConfig.create.success"));
        return "redirect:/inAppConfigs";
    }

    @GetMapping("/edit/{configID}")
    public String edit(@PathVariable(name = "configID") final UUID configID, final Model model) {
        model.addAttribute("inAppConfig", inAppConfigService.get(configID));
        return "inAppConfig/edit";
    }

    @PostMapping("/edit/{configID}")
    public String edit(@PathVariable(name = "configID") final UUID configID,
            @ModelAttribute("inAppConfig") @Valid final InAppConfigDTO inAppConfigDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inAppConfig/edit";
        }
        inAppConfigService.update(configID, inAppConfigDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("inAppConfig.update.success"));
        return "redirect:/inAppConfigs";
    }

    @PostMapping("/delete/{configID}")
    public String delete(@PathVariable(name = "configID") final UUID configID,
            final RedirectAttributes redirectAttributes) {
        inAppConfigService.delete(configID);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("inAppConfig.delete.success"));
        return "redirect:/inAppConfigs";
    }

}
