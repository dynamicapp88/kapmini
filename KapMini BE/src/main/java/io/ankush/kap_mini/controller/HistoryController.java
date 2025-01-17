package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.HistoryDTO;
import io.ankush.kap_mini.repos.AppRepository;
import io.ankush.kap_mini.repos.FormRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.service.HistoryService;
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
@RequestMapping("/histories")
public class HistoryController {

    private final HistoryService historyService;
    private final UserRepository userRepository;
    private final AppRepository appRepository;
    private final FormRepository formRepository;

    public HistoryController(final HistoryService historyService,
            final UserRepository userRepository, final AppRepository appRepository,
            final FormRepository formRepository) {
        this.historyService = historyService;
        this.userRepository = userRepository;
        this.appRepository = appRepository;
        this.formRepository = formRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userIdValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getName)));
        model.addAttribute("appIDValues", appRepository.findAll(Sort.by("appId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(App::getAppId, App::getName)));
        model.addAttribute("formIdValues", formRepository.findAll(Sort.by("formId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Form::getFormId, Form::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("histories", historyService.findAll());
        return "history/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("history") final HistoryDTO historyDTO) {
        return "history/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("history") @Valid final HistoryDTO historyDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "history/add";
        }
        historyService.create(historyDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("history.create.success"));
        return "redirect:/histories";
    }

    @GetMapping("/edit/{historyID}")
    public String edit(@PathVariable(name = "historyID") final UUID historyID, final Model model) {
        model.addAttribute("history", historyService.get(historyID));
        return "history/edit";
    }

    @PostMapping("/edit/{historyID}")
    public String edit(@PathVariable(name = "historyID") final UUID historyID,
            @ModelAttribute("history") @Valid final HistoryDTO historyDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "history/edit";
        }
        historyService.update(historyID, historyDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("history.update.success"));
        return "redirect:/histories";
    }

    @PostMapping("/delete/{historyID}")
    public String delete(@PathVariable(name = "historyID") final UUID historyID,
            final RedirectAttributes redirectAttributes) {
        historyService.delete(historyID);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("history.delete.success"));
        return "redirect:/histories";
    }

}
