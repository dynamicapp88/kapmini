package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.model.NotificationConfigurationDTO;
import io.ankush.kap_mini.service.NotificationConfigurationService;
import io.ankush.kap_mini.util.ReferencedWarning;
import io.ankush.kap_mini.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
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
@RequestMapping("/notificationConfigurations")
public class NotificationConfigurationController {

    private final NotificationConfigurationService notificationConfigurationService;

    public NotificationConfigurationController(
            final NotificationConfigurationService notificationConfigurationService) {
        this.notificationConfigurationService = notificationConfigurationService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("notificationConfigurations", notificationConfigurationService.findAll());
        return "notificationConfiguration/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("notificationConfiguration") final NotificationConfigurationDTO notificationConfigurationDTO) {
        return "notificationConfiguration/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("notificationConfiguration") @Valid final NotificationConfigurationDTO notificationConfigurationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "notificationConfiguration/add";
        }
        notificationConfigurationService.create(notificationConfigurationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("notificationConfiguration.create.success"));
        return "redirect:/notificationConfigurations";
    }

    @GetMapping("/edit/{configID}")
    public String edit(@PathVariable(name = "configID") final UUID configID, final Model model) {
        model.addAttribute("notificationConfiguration", notificationConfigurationService.get(configID));
        return "notificationConfiguration/edit";
    }

    @PostMapping("/edit/{configID}")
    public String edit(@PathVariable(name = "configID") final UUID configID,
            @ModelAttribute("notificationConfiguration") @Valid final NotificationConfigurationDTO notificationConfigurationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "notificationConfiguration/edit";
        }
        notificationConfigurationService.update(configID, notificationConfigurationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("notificationConfiguration.update.success"));
        return "redirect:/notificationConfigurations";
    }

    @PostMapping("/delete/{configID}")
    public String delete(@PathVariable(name = "configID") final UUID configID,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = notificationConfigurationService.getReferencedWarning(configID);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            notificationConfigurationService.delete(configID);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("notificationConfiguration.delete.success"));
        }
        return "redirect:/notificationConfigurations";
    }

}
