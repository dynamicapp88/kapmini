package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.Permission;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.RolePermissionDTO;
import io.ankush.kap_mini.repos.PermissionRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.service.RolePermissionService;
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
@RequestMapping("/rolePermissions")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public RolePermissionController(final RolePermissionService rolePermissionService,
            final UserRepository userRepository, final PermissionRepository permissionRepository) {
        this.rolePermissionService = rolePermissionService;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("roleIdValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getName)));
        model.addAttribute("premissionIdValues", permissionRepository.findAll(Sort.by("premissionId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Permission::getPremissionId, Permission::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("rolePermissions", rolePermissionService.findAll());
        return "rolePermission/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("rolePermission") final RolePermissionDTO rolePermissionDTO) {
        return "rolePermission/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("rolePermission") @Valid final RolePermissionDTO rolePermissionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "rolePermission/add";
        }
        rolePermissionService.create(rolePermissionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("rolePermission.create.success"));
        return "redirect:/rolePermissions";
    }

    @GetMapping("/edit/{rolePermissionId}")
    public String edit(@PathVariable(name = "rolePermissionId") final UUID rolePermissionId,
            final Model model) {
        model.addAttribute("rolePermission", rolePermissionService.get(rolePermissionId));
        return "rolePermission/edit";
    }

    @PostMapping("/edit/{rolePermissionId}")
    public String edit(@PathVariable(name = "rolePermissionId") final UUID rolePermissionId,
            @ModelAttribute("rolePermission") @Valid final RolePermissionDTO rolePermissionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "rolePermission/edit";
        }
        rolePermissionService.update(rolePermissionId, rolePermissionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("rolePermission.update.success"));
        return "redirect:/rolePermissions";
    }

    @PostMapping("/delete/{rolePermissionId}")
    public String delete(@PathVariable(name = "rolePermissionId") final UUID rolePermissionId,
            final RedirectAttributes redirectAttributes) {
        rolePermissionService.delete(rolePermissionId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("rolePermission.delete.success"));
        return "redirect:/rolePermissions";
    }

}
