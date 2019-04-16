package hello.Controllers;

import hello.*;
import hello.Services.*;
import hello.Devices.*;

import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CameraController {
    @Autowired
    private final CameraService cameraService;
    @Autowired
    private HubService hubService;

    public CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    @RequestMapping("/camera/{uuid}")
    public String camera(@PathVariable UUID uuid, Model model) {
        Camera device;
        Status status;

        device = (Camera)hubService.getDevice(uuid);
        status = device.getStatus();
        if (status == Status.ON) {
            model.addAttribute("cameraOnStatus", "ON");
            model.addAttribute("buttonValue", "Turn Off");
        } else if (status == Status.OFF) {
            model.addAttribute("cameraOnStatus", "OFF");
            model.addAttribute("buttonValue", "Start");
        }
        if (device.getRecordingStatus()) {
            model.addAttribute("cameraRecordingStatus", "ON");
        } else {
            model.addAttribute("cameraRecordingStatus", "OFF");
        }
        model.addAttribute("diskSize", device.getDiskSize());
        model.addAttribute("uuid", uuid);
        return "camera";
    }

    @RequestMapping("/camera/toggle/{uuid}")
    public ModelAndView cameraToggle(@PathVariable UUID uuid, Model model) {
        Camera device = (Camera)hubService.getDevice(uuid);
        cameraService.toggle(device);
        return new ModelAndView("redirect:/camera/" + uuid);
    }

    @RequestMapping("/camera/togglerecord/{uuid}")
    public ModelAndView cameraToggleRecord(@PathVariable UUID uuid, Model model) {
        Camera device = (Camera)hubService.getDevice(uuid);
        cameraService.toggleRecord(device);
        return new ModelAndView("redirect:/camera/" + uuid);
    }

    @RequestMapping("/camera/updatedisk/{uuid}")
    public @ResponseBody int cameraUpdateDisk(@PathVariable UUID uuid) {
        Camera device = (Camera)hubService.getDevice(uuid);
        cameraService.cameraUpdateDisk(device);
        return device.getDiskSize();
    }
}