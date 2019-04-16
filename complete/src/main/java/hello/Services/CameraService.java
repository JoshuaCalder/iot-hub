package hello.Services;

import hello.*;
import hello.Devices.*;

import org.springframework.stereotype.Service;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CameraService extends AbstractService {

    /**
     * Toggles camera on (started) or off
     * When camera is turned off, recording is forced to turn off as well
     */
    public void toggle(Camera c) {
        Status currStatus = c.getStatus();
        if (currStatus == Status.OFF) {
            c.setStatus(Status.ON);
            writeLog("Camera " + c.getIdentifier() + " turned ON");
        } else {
            c.setStatus(Status.OFF);
            c.setRecordingStatus(false);
            writeLog("Camera " + c.getIdentifier() + " turned OFF");
        }
        updateDatabase();
    }

    /**
     * Toggles camera from recording to not recording
     */
    public void toggleRecord(Camera c) {
        if (c.getRecordingStatus()) {
            c.setRecordingStatus(false);
        } else {
            c.setRecordingStatus(true);
        }
    }

    /**
     * Update the camera disk size
     */
    public void cameraUpdateDisk(Camera c) {
        int currDiskSize = c.getDiskSize();
        c.setDiskSize(currDiskSize + 1);
    }
}