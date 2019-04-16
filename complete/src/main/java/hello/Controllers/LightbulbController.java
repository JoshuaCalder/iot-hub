package hello.Controllers;

import hello.*;
import hello.Services.*;
import hello.Devices.*;

import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LightbulbController {
    @Autowired
    private LightbulbService lightbulbService;
    @Autowired
    private HubService hubService;

    public LightbulbController(LightbulbService lightbulbService) {
        this.lightbulbService = lightbulbService;
    }

    @RequestMapping("/lightbulb/toggle/{uuid}")
    public ModelAndView lightbulbToggle(@PathVariable UUID uuid, Model model) {
        Lightbulb device = (Lightbulb)hubService.getDevice(uuid);
        lightbulbService.toggle(device);
        return new ModelAndView("redirect:/lightbulb/" + uuid);
    }

    @RequestMapping("/lightbulb/{uuid}")
    public String lightbulb(@PathVariable UUID uuid, Model model) {
        Lightbulb device;
        Status deviceStatus;

        device = (Lightbulb)hubService.getDevice(uuid);
        deviceStatus = lightbulbService.getCondition(device);
        if (deviceStatus == Status.ON) {
            model.addAttribute("lightbulbStatus", "ON");
        } else if (deviceStatus == Status.OFF) {
            model.addAttribute("lightbulbStatus", "OFF");
        }
        model.addAttribute("uuid", uuid);
        return "lightbulb";
    }
}