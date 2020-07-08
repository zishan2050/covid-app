package com.app.covid.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/covid/app")
public class CovidViewController {

    @RequestMapping(value = "/login.html")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (!StringUtils.isBlank(error)) {
            model.addAttribute("loginError", true);
        }
        return "login.html";
    }

    @RequestMapping(value = "/sign_up.html")
    public String signUp(@RequestParam(value = "error", required = false) String error, Model model) {
        if (!StringUtils.isBlank(error)) {
            model.addAttribute("signUpError", true);
        }
        return "sign_up.html";
    }

    @RequestMapping(value = "/home.html")
    public String home() {
        return "home.html";
    }
}
