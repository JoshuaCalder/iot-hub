package hello.Controllers;

import hello.*;
import hello.Services.*;
import hello.Devices.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class LoginController {

    @Autowired
    private HubService hubService;

    @GetMapping("/user/registration")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("userTypes", UserTypes.values());
        return "userRegistration";
    }

    @PostMapping("/user/registration")
    public ModelAndView registrationSubmit(Model model, @ModelAttribute User user) throws HubRegistrationException {
        try {
            hubService.registerUser(user);
        } catch (HubRegistrationException e) {
            model.addAttribute("error", "User already exists. Try again.");
            return new ModelAndView("redirect:/user/registration");
        }
        return new ModelAndView("result", "model", model);
    }

    @GetMapping("/user/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "userLogin";
    }

    @PostMapping("/user/login")
    public ModelAndView loginValidate(Model model, @ModelAttribute User user, RedirectAttributes redirectAttributes)
                                                                                    throws WrongPasswordException,
                                                                                     NoSuchUserException {
        User validatedUser;
        try {
            validatedUser = hubService.validateUser(user);
        } catch (WrongPasswordException e) {
            model.addAttribute("error", e.getMessage());
            return new ModelAndView("userLogin", "model", model);
        } catch (NoSuchUserException e1) {
            model.addAttribute("error", e1.getMessage());
            return new ModelAndView("userLogin", "model", model);
        }
        hubService.setCurrentUserToHub(validatedUser);
        return new ModelAndView("redirect:/hub");
    }

    @RequestMapping("/user/logout")
    public ModelAndView userLogout(Model model) {
        hubService.setCurrentUserToHub(new User());
        return new ModelAndView("redirect:/user/login");
    }

    @RequestMapping("/user/remove/{userName}")
    public ModelAndView userRemove(@PathVariable String userName, Model model) throws HubRegistrationException {
        hubService.unregisterUser(userName);
        return new ModelAndView("redirect:/hub");
    }

    @RequestMapping("/user/control/{userName}")
    public ModelAndView userControl(@PathVariable String userName, Model model) throws HubRegistrationException {
        hubService.setLogVis(userName);
        return new ModelAndView("redirect:/hub");
    }
}

































