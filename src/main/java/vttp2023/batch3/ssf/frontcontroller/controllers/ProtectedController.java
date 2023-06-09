package vttp2023.batch3.ssf.frontcontroller.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/protected")
public class ProtectedController {

	// TODO Task 5
	// Write a controller to protect resources rooted under /protected
    @GetMapping(path = {"", "*"},
            produces = MediaType.TEXT_HTML_VALUE)
    public String getAuthenticated(HttpSession session) {
        if (session.getAttribute("authenticatedAs") == null) {
            return "redirect:/";
        }
        return "protected/view1";
    }
}
