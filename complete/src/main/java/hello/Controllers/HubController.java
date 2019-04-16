package hello.Controllers;

import hello.*;
import hello.Services.*;
import hello.Devices.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HubController extends AbstractService {
    @Autowired
    private final HubService service;

    public HubController(HubService service) {
        this.service = service;
    }

    @RequestMapping("/hub")
    public ModelAndView hub(Model model) throws HubRegistrationException {
        Map<UUID, Device> deviceList = service.returnDevices();
        String[] logs = service.getLog();
        Map<String, User> userList = service.returnUsers();
        String userName = service.getCurrentUserName();
        if (userName == null) {
            User u = new User("username", "password", UserTypes.ADMIN);
            service.setCurrentUserToHub(u);
        }

        boolean isAdmin = service.isCurrentUserAdmin();

        ModelAndView mv = new ModelAndView();
        mv.addObject("userList", userList);
        mv.setViewName("hub");
        mv.addObject("deviceList", deviceList);
        mv.addObject("logs", logs);
        mv.addObject("userName", userName);
        mv.addObject("isAdmin", isAdmin);
        mv.addObject("canSeeLog", service.getCurrentUserFromHub().isCanSeeLog());

        return mv;
    }

    @RequestMapping("/hub/shutdown")
    public ModelAndView shutdownHub(Model model) {
        service.shutdown();
        return new ModelAndView("redirect:/hub");
    }

    @RequestMapping("/lightbulb/add")
    public ModelAndView lightbulbAdd(Model model) throws HubRegistrationException {
        service.addLightbulb();
        return new ModelAndView("redirect:/hub");
    }

    @RequestMapping("/smartplug/add")
    public ModelAndView smartplugAdd(Model model) throws HubRegistrationException {
        service.addSmartPlug();
        return new ModelAndView("redirect:/hub");
    }

    @RequestMapping("/thermostat/add")
    public ModelAndView thermostatAdd(Model model) throws HubRegistrationException, Temperature.TemperatureOutofBoundsException {
        service.addThermostat();
        return new ModelAndView("redirect:/hub");
    }

    @RequestMapping("/camera/add")
    public ModelAndView cameraAdd(Model model) throws HubRegistrationException {
        service.addCamera();
        return new ModelAndView("redirect:/hub");
    }

    // get device from uuid, call unregister(Device)
    @RequestMapping("/remove/{uuid}")
    public ModelAndView removeDevice(@PathVariable UUID uuid, Model model) throws HubRegistrationException {
        Device device = service.getDevice(uuid);
        service.unregisterDevice(device);
        return new ModelAndView("redirect:/hub/");
    }

    @RequestMapping("/check_event")
    public @ResponseBody String checkRandomEventStatus(Model model) {
        return service.getRandomEvents();
    }

    @RequestMapping("/edit/{uuid}")
    public ModelAndView editDevice(@PathVariable UUID uuid, Model model) {
        Device device = service.getDevice(uuid);
        String c = device.getClass().getSimpleName();
        if (c.equals("Lightbulb")) {
            return new ModelAndView("redirect:/lightbulb/" + uuid);
        } else if (c.equals("SmartPlug")) {
            return new ModelAndView("redirect:/smartplug/" + uuid);
        } else if (c.equals("Thermostat")) {
            return new ModelAndView("redirect:/thermostat/" + uuid);
        } else if (c.equals("Camera")){
            return new ModelAndView("redirect:/camera/" + uuid);
        }
        // unrecognized device type defaults to hub
        return new ModelAndView("redirect:/hub/");
    }
}