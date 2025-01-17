package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.model.PermissionDTO;
import io.ankush.kap_mini.service.PermissionService;
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
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(final PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("permissions", permissionService.findAll());
        return "permission/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("permission") final PermissionDTO permissionDTO) {
        return "permission/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("permission") @Valid final PermissionDTO permissionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "permission/add";
        }
        permissionService.create(permissionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("permission.create.success"));
        return "redirect:/permissions";
    }

    @GetMapping("/edit/{premissionId}")
    public String edit(@PathVariable(name = "premissionId") final UUID premissionId,
            final Model model) {
        model.addAttribute("permission", permissionService.get(premissionId));
        return "permission/edit";
    }

    @PostMapping("/edit/{premissionId}")
    public String edit(@PathVariable(name = "premissionId") final UUID premissionId,
            @ModelAttribute("permission") @Valid final PermissionDTO permissionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "permission/edit";
        }
        permissionService.update(premissionId, permissionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("permission.update.success"));
        return "redirect:/permissions";
    }

    @PostMapping("/delete/{premissionId}")
    public String delete(@PathVariable(name = "premissionId") final UUID premissionId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = permissionService.getReferencedWarning(premissionId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            permissionService.delete(premissionId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("permission.delete.success"));
        }
        return "redirect:/permissions";
    }

}
