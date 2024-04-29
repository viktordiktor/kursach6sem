package com.nikonenko.kursach6sem.controllers;

import com.nikonenko.kursach6sem.dto.responses.RecreationObjectDto;
import com.nikonenko.kursach6sem.services.RecreationObjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/recreation-objects")
public class RecreationObjectController {
    private final RecreationObjectService recreationObjectService;

    @GetMapping("/{id}")
    public String getRecreationObject(Model model, @PathVariable("id") Long id) {
        model.addAttribute("object", recreationObjectService.getRecreationObjectById(id));
        return "objects/show";
    }

    @GetMapping
    public String getAll(Model model, @PageableDefault(size = 8) Pageable pageable) {
        model.addAttribute("pagedObjects", recreationObjectService.getPageableRecreationObjects(pageable));
        return "objects/index";
    }

    @GetMapping("/search")
    public String searchAndSort(Model model,
                                @RequestParam(name = "text", required = false) String search,
                                @RequestParam(name = "sortField", required = false) String sortField,
                                @PageableDefault(size = 8) Pageable pageable) {
        model.addAttribute("pagedObjects",
                recreationObjectService.getSearchRecreationObjects(search, sortField, pageable));
        return "objects/search";
    }

    @GetMapping("/new")
    public String getNewObjectPage(@ModelAttribute("object") RecreationObjectDto recreationObjectDto) {
        return "objects/new";
    }

    @GetMapping("/edit/{id}")
    public String getEditBookPage(@PathVariable Long id, Model model) {
        model.addAttribute("object", recreationObjectService.getRecreationObjectById(id));
        return "objects/edit";
    }

    @PostMapping
    public String addObject(@ModelAttribute("object") @Valid RecreationObjectDto recreationObjectDto,
                            BindingResult bindingResult,
                            @RequestParam("img") MultipartFile file,
                            @RequestParam("additionalImages") MultipartFile[] additionalImages) {
        if (bindingResult.hasErrors())
            return "objects/new";
        recreationObjectService.saveRecreationObject(recreationObjectDto, file, additionalImages);
        return "redirect:/api/v1/users/admin/recreation-objects";
    }

    @PatchMapping("/{id}")
    public String editObject(@PathVariable Long id, @ModelAttribute("object") RecreationObjectDto recreationObjectDto,
                             @RequestParam("img") MultipartFile file,
                             @RequestParam("additionalImages") MultipartFile[] additionalImages) {
        recreationObjectService.editRecreationObject(id, recreationObjectDto, file, additionalImages);
        return "redirect:/api/v1/users/admin/recreation-objects";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        recreationObjectService.deleteRecreationObject(id);
        return "redirect:/api/v1/users/admin/recreation-objects";
    }
}
