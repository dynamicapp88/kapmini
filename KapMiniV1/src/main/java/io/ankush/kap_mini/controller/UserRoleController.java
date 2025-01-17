package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.Role;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.UserRoleDTO;
import io.ankush.kap_mini.repos.RoleRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.service.UserRoleService;
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
@RequestMapping("/userRoles")
public class UserRoleController {

    private final UserRoleService userRoleService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleController(final UserRoleService userRoleService,
            final UserRepository userRepository, final RoleRepository roleRepository) {
        this.userRoleService = userRoleService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userIdValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getName)));
        model.addAttribute("roleIdValues", roleRepository.findAll(Sort.by("roleId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Role::getRoleId, Role::getRoleName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("userRoles", userRoleService.findAll());
        return "userRole/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("userRole") final UserRoleDTO userRoleDTO) {
        return "userRole/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("userRole") @Valid final UserRoleDTO userRoleDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "userRole/add";
        }
        userRoleService.create(userRoleDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("userRole.create.success"));
        return "redirect:/userRoles";
    }

    @GetMapping("/edit/{userRoleId}")
    public String edit(@PathVariable(name = "userRoleId") final UUID userRoleId,
            final Model model) {
        model.addAttribute("userRole", userRoleService.get(userRoleId));
        return "userRole/edit";
    }

    @PostMapping("/edit/{userRoleId}")
    public String edit(@PathVariable(name = "userRoleId") final UUID userRoleId,
            @ModelAttribute("userRole") @Valid final UserRoleDTO userRoleDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "userRole/edit";
        }
        userRoleService.update(userRoleId, userRoleDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("userRole.update.success"));
        return "redirect:/userRoles";
    }

    @PostMapping("/delete/{userRoleId}")
    public String delete(@PathVariable(name = "userRoleId") final UUID userRoleId,
            final RedirectAttributes redirectAttributes) {
        userRoleService.delete(userRoleId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("userRole.delete.success"));
        return "redirect:/userRoles";
    }

}
