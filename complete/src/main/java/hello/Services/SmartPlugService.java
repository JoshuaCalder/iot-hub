package hello.Services;

import hello.*;
import hello.Devices.*;

import org.springframework.stereotype.Service;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SmartPlugService extends AbstractService {

    public void toggle(SmartPlug s) {
        Status currStatus = s.getStatus();
        if (currStatus == Status.OFF) {
            s.setStatus(Status.ON);
            writeLog("Smartplug " + s.getIdentifier() + " turned ON");
        } else {
            s.setStatus(Status.OFF);
            writeLog("Smartplug " + s.getIdentifier() + " turned OFF");
        }
        updateDatabase();
    }

    public Status getCondition(SmartPlug s) {
        return s.getStatus();
    }
}