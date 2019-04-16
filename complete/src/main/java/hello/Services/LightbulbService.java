package hello.Services;

import hello.*;
import hello.Devices.*;

import org.springframework.stereotype.Service;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
public class LightbulbService extends AbstractService {

    /**
     * Toggles specified lightbulb status NORMAL/OFF
     * @param l Lightbulb object to be toggled
     */
    public void toggle(Lightbulb l) {
        Status currStatus = l.getStatus();
        if (currStatus == Status.OFF) {
            l.setStatus(Status.ON);
            writeLog("Lightbulb " + l.getIdentifier() + " turned ON");
        } else {
            l.setStatus(Status.OFF);
            writeLog("Lightbulb " + l.getIdentifier() + " turned OFF");
        }
        updateDatabase();
    }

    /**
     * Gets current status of lightbulb
     */
    public Status getCondition(Lightbulb l) {
        return l.getStatus();
    }
}