package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.model.WhatsAppConfigDTO;
import io.ankush.kap_mini.repos.NotificationConfigurationTypeRepository;
import io.ankush.kap_mini.service.WhatsAppConfigService;
import io.ankush.kap_mini.util.CustomCollectors;
import io.ankush.kap_mini.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/whatsAppConfigs")
public class WhatsAppConfigController {

    private final WhatsAppConfigService whatsAppConfigService;
    private final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository;

    public WhatsAppConfigController(final WhatsAppConfigService whatsAppConfigService,
            final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository) {
        this.whatsAppConfigService = whatsAppConfigService;
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
        model.addAttribute("whatsAppConfigs", whatsAppConfigService.findAll());
        return "whatsAppConfig/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("whatsAppConfig") final WhatsAppConfigDTO whatsAppConfigDTO) {
        return "whatsAppConfig/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("whatsAppConfig") @Valid final WhatsAppConfigDTO whatsAppConfigDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "whatsAppConfig/add";
        }
        whatsAppConfigService.create(whatsAppConfigDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("whatsAppConfig.create.success"));
        return "redirect:/whatsAppConfigs";
    }

    @GetMapping("/edit/{configID}")
    public String edit(@PathVariable(name = "configID") final Long configID, final Model model) {
        model.addAttribute("whatsAppConfig", whatsAppConfigService.get(configID));
        return "whatsAppConfig/edit";
    }

    @PostMapping("/edit/{configID}")
    public String edit(@PathVariable(name = "configID") final Long configID,
            @ModelAttribute("whatsAppConfig") @Valid final WhatsAppConfigDTO whatsAppConfigDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "whatsAppConfig/edit";
        }
        whatsAppConfigService.update(configID, whatsAppConfigDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("whatsAppConfig.update.success"));
        return "redirect:/whatsAppConfigs";
    }

    @PostMapping("/delete/{configID}")
    public String delete(@PathVariable(name = "configID") final Long configID,
            final RedirectAttributes redirectAttributes) {
        whatsAppConfigService.delete(configID);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("whatsAppConfig.delete.success"));
        return "redirect:/whatsAppConfigs";
    }

}
