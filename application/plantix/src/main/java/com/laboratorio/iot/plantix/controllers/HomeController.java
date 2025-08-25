package com.laboratorio.iot.plantix.controllers;

import com.laboratorio.iot.plantix.helpers.ViewRouterHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Plantix - Monitoreo de Plantas IoT");

        return ViewRouterHelper.INDEX;// Retorna la vista index.html ubicada en src/main/resources/templates
    }
}
