package alledrogo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Core application class
 */
@RestController
public class AlledrogoController {
    @GetMapping("/main")
    public ModelAndView mainPage() {
        return new ModelAndView("main");
    }
    @GetMapping("/bought-products")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ModelAndView boughtProducts() {
        return new ModelAndView("bought-products");
    }
    @GetMapping("/my-products")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ModelAndView myProducts() {
        return new ModelAndView("my-products");
    }
    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ModelAndView myProfile() {
        return new ModelAndView("my-profile");
    }
    @GetMapping("/category/{category}")
    public ModelAndView filteredProducts() {
        return new ModelAndView("category");
    }
}
