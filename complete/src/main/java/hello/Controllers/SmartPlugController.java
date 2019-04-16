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
public class SmartPlugController {
    @Autowired
    private SmartPlugService smartplugService;
    @Autowired
    private HubService hubService;

    public SmartPlugController(SmartPlugService smartplugService) {
        this.smartplugService = smartplugService;
    }

    @RequestMapping("/smartplug/toggle/{uuid}")
    public ModelAndView smartplugToggle(@PathVariable UUID uuid, Model model) {
        SmartPlug device = (SmartPlug)hubService.getDevice(uuid);
        smartplugService.toggle(device);
        return new ModelAndView("redirect:/smartplug/" + uuid);
    }

    @RequestMapping("/smartplug/{uuid}")
    public String smartplug(@PathVariable UUID uuid, Model model) {
        SmartPlug device;
        Status deviceStatus;

        device = (SmartPlug)hubService.getDevice(uuid);
        deviceStatus = smartplugService.getCondition(device);
        if (deviceStatus == Status.ON) {
            model.addAttribute("smartplugStatus", "ON");
        } else if (deviceStatus == Status.OFF) {
            model.addAttribute("smartplugStatus", "OFF");
        }
        model.addAttribute("uuid", uuid);
        return "smartplug";
    }
}