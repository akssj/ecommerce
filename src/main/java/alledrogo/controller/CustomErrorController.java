package alledrogo.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return new ModelAndView("/error/error404", HttpStatus.NOT_FOUND);
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return new ModelAndView("/error/error500", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            else if(statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return new ModelAndView("/error/error401", HttpStatus.UNAUTHORIZED);
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return new ModelAndView("/error/error403", HttpStatus.FORBIDDEN);
            }
        }

        return new ModelAndView("/error/error404", HttpStatus.NOT_FOUND);
    }
}
