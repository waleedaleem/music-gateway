package com.walid.music;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * redirects error requests to a custom error page
 * 
 * @author wmoustaf
 */
@Controller
@RequestMapping(value = "/error")
public class ErrorController {

    private static final String ERROR_PAGE = "error";

    public String error() {
        return ERROR_PAGE;
    }
}
