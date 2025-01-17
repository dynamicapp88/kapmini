package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.AuditLogDTO;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.service.AuditLogService;
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
@RequestMapping("/auditLogs")
public class AuditLogController {

    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    public AuditLogController(final AuditLogService auditLogService,
            final UserRepository userRepository) {
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userIdValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("auditLogs", auditLogService.findAll());
        return "auditLog/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("auditLog") final AuditLogDTO auditLogDTO) {
        return "auditLog/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("auditLog") @Valid final AuditLogDTO auditLogDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auditLog/add";
        }
        auditLogService.create(auditLogDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("auditLog.create.success"));
        return "redirect:/auditLogs";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id, final Model model) {
        model.addAttribute("auditLog", auditLogService.get(id));
        return "auditLog/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final UUID id,
            @ModelAttribute("auditLog") @Valid final AuditLogDTO auditLogDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auditLog/edit";
        }
        auditLogService.update(id, auditLogDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("auditLog.update.success"));
        return "redirect:/auditLogs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final UUID id,
            final RedirectAttributes redirectAttributes) {
        auditLogService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("auditLog.delete.success"));
        return "redirect:/auditLogs";
    }

}
