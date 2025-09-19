package com.laboratorio.iot.plantix.controllers;

import com.laboratorio.iot.plantix.helpers.ViewRouterHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/login")
    public String login() {
        return ViewRouterHelper.LOGIN;
    }
    @GetMapping("/login-success")
    public String loginSuccess() {
        return ViewRouterHelper.INDEX;
    }
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth/login";
    }
}
