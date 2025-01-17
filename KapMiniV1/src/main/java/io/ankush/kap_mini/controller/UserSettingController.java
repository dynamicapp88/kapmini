package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.Language;
import io.ankush.kap_mini.model.Theme;
import io.ankush.kap_mini.model.UserSettingDTO;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.service.UserSettingService;
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
@RequestMapping("/userSettings")
public class UserSettingController {

    private final UserSettingService userSettingService;
    private final UserRepository userRepository;

    public UserSettingController(final UserSettingService userSettingService,
            final UserRepository userRepository) {
        this.userSettingService = userSettingService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("themeValues", Theme.values());
        model.addAttribute("languageValues", Language.values());
        model.addAttribute("userIdValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("userSettings", userSettingService.findAll());
        return "userSetting/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("userSetting") final UserSettingDTO userSettingDTO) {
        return "userSetting/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("userSetting") @Valid final UserSettingDTO userSettingDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "userSetting/add";
        }
        userSettingService.create(userSettingDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("userSetting.create.success"));
        return "redirect:/userSettings";
    }

    @GetMapping("/edit/{settingID}")
    public String edit(@PathVariable(name = "settingID") final UUID settingID, final Model model) {
        model.addAttribute("userSetting", userSettingService.get(settingID));
        return "userSetting/edit";
    }

    @PostMapping("/edit/{settingID}")
    public String edit(@PathVariable(name = "settingID") final UUID settingID,
            @ModelAttribute("userSetting") @Valid final UserSettingDTO userSettingDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "userSetting/edit";
        }
        userSettingService.update(settingID, userSettingDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("userSetting.update.success"));
        return "redirect:/userSettings";
    }

    @PostMapping("/delete/{settingID}")
    public String delete(@PathVariable(name = "settingID") final UUID settingID,
            final RedirectAttributes redirectAttributes) {
        userSettingService.delete(settingID);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("userSetting.delete.success"));
        return "redirect:/userSettings";
    }

}
