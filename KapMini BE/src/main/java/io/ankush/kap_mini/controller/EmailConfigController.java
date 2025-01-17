package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.NotificationConfigurationType;
import io.ankush.kap_mini.model.EmailConfigDTO;
import io.ankush.kap_mini.repos.NotificationConfigurationTypeRepository;
import io.ankush.kap_mini.service.EmailConfigService;
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
@RequestMapping("/emailConfigs")
public class EmailConfigController {

    private final EmailConfigService emailConfigService;
    private final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository;

    public EmailConfigController(final EmailConfigService emailConfigService,
            final NotificationConfigurationTypeRepository notificationConfigurationTypeRepository) {
        this.emailConfigService = emailConfigService;
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
        model.addAttribute("emailConfigs", emailConfigService.findAll());
        return "emailConfig/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("emailConfig") final EmailConfigDTO emailConfigDTO) {
        return "emailConfig/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("emailConfig") @Valid final EmailConfigDTO emailConfigDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "emailConfig/add";
        }
        emailConfigService.create(emailConfigDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("emailConfig.create.success"));
        return "redirect:/emailConfigs";
    }

    @GetMapping("/edit/{configId}")
    public String edit(@PathVariable(name = "configId") final UUID configId, final Model model) {
        model.addAttribute("emailConfig", emailConfigService.get(configId));
        return "emailConfig/edit";
    }

    @PostMapping("/edit/{configId}")
    public String edit(@PathVariable(name = "configId") final UUID configId,
            @ModelAttribute("emailConfig") @Valid final EmailConfigDTO emailConfigDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "emailConfig/edit";
        }
        emailConfigService.update(configId, emailConfigDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("emailConfig.update.success"));
        return "redirect:/emailConfigs";
    }

    @PostMapping("/delete/{configId}")
    public String delete(@PathVariable(name = "configId") final UUID configId,
            final RedirectAttributes redirectAttributes) {
        emailConfigService.delete(configId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("emailConfig.delete.success"));
        return "redirect:/emailConfigs";
    }

}
