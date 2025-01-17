package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.NotificationConfiguration;
import io.ankush.kap_mini.model.NotificationConfigurationTypeDTO;
import io.ankush.kap_mini.model.NotificationType;
import io.ankush.kap_mini.repos.NotificationConfigurationRepository;
import io.ankush.kap_mini.service.NotificationConfigurationTypeService;
import io.ankush.kap_mini.util.CustomCollectors;
import io.ankush.kap_mini.util.ReferencedWarning;
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
@RequestMapping("/notificationConfigurationTypes")
public class NotificationConfigurationTypeController {

    private final NotificationConfigurationTypeService notificationConfigurationTypeService;
    private final NotificationConfigurationRepository notificationConfigurationRepository;

    public NotificationConfigurationTypeController(
            final NotificationConfigurationTypeService notificationConfigurationTypeService,
            final NotificationConfigurationRepository notificationConfigurationRepository) {
        this.notificationConfigurationTypeService = notificationConfigurationTypeService;
        this.notificationConfigurationRepository = notificationConfigurationRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("notificationTypeValues", NotificationType.values());
        model.addAttribute("configIdValues", notificationConfigurationRepository.findAll(Sort.by("configID"))
                .stream()
                .collect(CustomCollectors.toSortedMap(NotificationConfiguration::getConfigID, NotificationConfiguration::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("notificationConfigurationTypes", notificationConfigurationTypeService.findAll());
        return "notificationConfigurationType/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("notificationConfigurationType") final NotificationConfigurationTypeDTO notificationConfigurationTypeDTO) {
        return "notificationConfigurationType/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("notificationConfigurationType") @Valid final NotificationConfigurationTypeDTO notificationConfigurationTypeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "notificationConfigurationType/add";
        }
        notificationConfigurationTypeService.create(notificationConfigurationTypeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("notificationConfigurationType.create.success"));
        return "redirect:/notificationConfigurationTypes";
    }

    @GetMapping("/edit/{configTypeID}")
    public String edit(@PathVariable(name = "configTypeID") final UUID configTypeID,
            final Model model) {
        model.addAttribute("notificationConfigurationType", notificationConfigurationTypeService.get(configTypeID));
        return "notificationConfigurationType/edit";
    }

    @PostMapping("/edit/{configTypeID}")
    public String edit(@PathVariable(name = "configTypeID") final UUID configTypeID,
            @ModelAttribute("notificationConfigurationType") @Valid final NotificationConfigurationTypeDTO notificationConfigurationTypeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "notificationConfigurationType/edit";
        }
        notificationConfigurationTypeService.update(configTypeID, notificationConfigurationTypeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("notificationConfigurationType.update.success"));
        return "redirect:/notificationConfigurationTypes";
    }

    @PostMapping("/delete/{configTypeID}")
    public String delete(@PathVariable(name = "configTypeID") final UUID configTypeID,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = notificationConfigurationTypeService.getReferencedWarning(configTypeID);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            notificationConfigurationTypeService.delete(configTypeID);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("notificationConfigurationType.delete.success"));
        }
        return "redirect:/notificationConfigurationTypes";
    }

}
