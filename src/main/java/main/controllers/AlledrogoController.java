package main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/main")
public class AlledrogoController {
    @GetMapping
    public ModelAndView showMainPage() {
        ModelAndView modelAndView = new ModelAndView("main");
        return modelAndView;
    }
}
