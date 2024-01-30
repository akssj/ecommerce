package alledrogo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Core application class
 */
@RestController
@RequestMapping("/")
public class AlledrogoController {
    @GetMapping("/main")
    public ModelAndView mainPage() {
        return new ModelAndView("main");
    }
    @GetMapping("/bought-products")
    public ModelAndView boughtProducts() {
        return new ModelAndView("bought-products");
    }
    @GetMapping("/my-products")
    public ModelAndView myProducts() {
        return new ModelAndView("my-products");
    }
    @GetMapping("/my-profile")
    public ModelAndView myProfile() {
        return new ModelAndView("my-profile");
    }
    @GetMapping("/category/{category}")
    public ModelAndView filteredProducts() {
        return new ModelAndView("category");
    }
}
