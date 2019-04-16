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
public class ThermostatController {
    @Autowired
    private final ThermostatService thermostatService;
    @Autowired
    private HubService hubService;

    public ThermostatController(ThermostatService thermostatService) {
        this.thermostatService = thermostatService;
    }

    @RequestMapping("/thermostat/{uuid}")
    public String thermostat(@PathVariable UUID uuid, Model model) {
        Thermostat device;
        Status deviceStatus;
        Temperature temp;

        device = (Thermostat)hubService.getDevice(uuid);
        deviceStatus = device.getStatus();
        if (deviceStatus == Status.ON) {
            model.addAttribute("thermostatStatus", "ON");
            model.addAttribute("buttonValue", "Turn Off");
        } else if (deviceStatus == Status.OFF) {
            model.addAttribute("thermostatStatus", "OFF");
            model.addAttribute("buttonValue", "Start");
        }

        temp = device.getTemp();
        model.addAttribute("temperature", String.valueOf(temp.getTemperature()));
        model.addAttribute("unit", temp.getUnit().toString());
        model.addAttribute("uuid", uuid);

        return "thermostat";
    }

    @RequestMapping("/thermostat/toggle/{uuid}")
    public ModelAndView thermostatStart(@PathVariable UUID uuid, Model model) {
        Thermostat device = (Thermostat)hubService.getDevice(uuid);
        thermostatService.toggle(device);
        return new ModelAndView("redirect:/thermostat/" + uuid);
    }

    @RequestMapping("thermostat/convert/{uuid}")
    public ModelAndView thermostatConvert(@PathVariable UUID uuid, Model model) {
        Thermostat device = (Thermostat)hubService.getDevice(uuid);
        thermostatService.convertUnit(device);
        return new ModelAndView("redirect:/thermostat/" + uuid);
    }

    @RequestMapping("thermostat/increasetemp/{uuid}")
    public ModelAndView thermostatIncreaseTemp(@PathVariable UUID uuid, Model model) {
        Thermostat device = (Thermostat)hubService.getDevice(uuid);
        thermostatService.increaseTemp(device);
        return new ModelAndView("redirect:/thermostat/" + uuid);
    }

    @RequestMapping("thermostat/decreasetemp/{uuid}")
    public ModelAndView thermostatDecreaseTemp(@PathVariable UUID uuid, Model model) {
        Thermostat device = (Thermostat)hubService.getDevice(uuid);
        thermostatService.decreaseTemp(device);
        return new ModelAndView("redirect:/thermostat/" + uuid);
    }
}