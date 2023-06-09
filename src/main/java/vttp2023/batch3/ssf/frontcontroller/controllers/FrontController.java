package vttp2023.batch3.ssf.frontcontroller.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import vttp2023.batch3.ssf.frontcontroller.model.Captcha;
import vttp2023.batch3.ssf.frontcontroller.model.User;
import vttp2023.batch3.ssf.frontcontroller.services.AuthenticationService;

@Controller
@RequestMapping
public class FrontController {
    @Autowired
    private AuthenticationService service;

    private Captcha handleCaptcha(HttpSession session) {
        session.removeAttribute("captcha");
        Captcha captcha = new Captcha();
        session.setAttribute("captcha", captcha);
        return captcha;
    }

	// TODO: Task 2, Task 3, Task 4, Task 6
	@GetMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getLanding(Model model, HttpSession session) {
        session.removeAttribute("authenticatedAs");

        if (session.getAttribute("captcha") != null) {
            Captcha captcha = this.handleCaptcha(session);
            model.addAttribute("captcha", captcha);
        }

        model.addAttribute("user", new User());
        return "view0";
    }

    @GetMapping(path = "login",
            produces = MediaType.TEXT_HTML_VALUE)
    public String getLogin(HttpSession session) {
        if (session.getAttribute("authenticatedAs") != null) {
            return "redirect:/protected";
        }
        return "redirect:/";
    }

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    public String postLogin(@Valid @ModelAttribute User user, BindingResult bindingResult, @RequestParam(required = false) String captchaAnswer, Model model, HttpSession session) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        Captcha retrievedCaptcha = (Captcha) session.getAttribute("captcha");

        model.addAttribute("user", user);

        if (bindingResult.hasErrors()) {
            return "view0";
        }

        if (service.isLocked(username)) {
            return "view2";
        }

        if (retrievedCaptcha != null && !Captcha.validateCaptcha(captchaAnswer, retrievedCaptcha)) {
            service.saveFailedAuthAttempt(username);
            bindingResult.addError(new ObjectError("captchaFailed", "Failed captcha challenge!"));
            Captcha captcha = this.handleCaptcha(session);
            model.addAttribute("captcha", captcha);
            return "view0";
        }

        try {
            service.authenticate(username, password);
        } catch (HttpClientErrorException e) {
            service.saveFailedAuthAttempt(username);
            bindingResult.addError(new ObjectError("loginFailed", e.getStatusText()));
            Captcha captcha = this.handleCaptcha(session);
            model.addAttribute("captcha", captcha);
            return "view0";
        }

        session.removeAttribute("captcha");
        session.setAttribute("authenticatedAs", username);
        return "redirect:/protected";
    }
}
