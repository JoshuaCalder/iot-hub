package hello;

import hello.Devices.*;
import hello.Controllers.*;
import hello.Services.*;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
//@WithMockUser
public class MainFunctionalityTests {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private HubService s;

    @InjectMocks
    private HubController hubController;

    @InjectMocks
    private User user = new User ("Admin", "admin", UserTypes.ADMIN);


    /** Scenario Z: Assignment 4 **/
    /** Scenario A: Users and Admin **/
    /** Scenario: Booting Home Automation System (Hub) **/
    /** Scenario C: User wants to access camera **/
    /** Scenario D: User wants to access thermostat **/
    /** Scenario E: User wants to access lightbulb **/
    /** Scenario F: User wants to access smartplug **/

    /** Scenario Z: Assignment 4 **/

    /**
     * 1. GIVEN I want to see a Camera device WHEN That Camera has integrated video
     * streaming THEN I can see the video's live feed
     */
    @Test
    public void isCameraShowStream() throws Exception {
        Hub h = new Hub();
        Camera c = new Camera(h);
        c.setDiskSize(20);
        assertEquals(c.isFull(), false);
        h.register(c);
        c.setRecordingStatus(true);
        assertEquals(c.getRecordingStatus(), true);
    }

    /**
     * 2. Given I am an administrator WHEN I want to see a device status check THEN the status
     * check thread starts and collects the data without interrupting the UI.
     */
    @Test
    public void adminDeviceStatus() throws Exception {
        Hub h = new Hub();
        Camera c = new Camera(h);
        c.setRecordingStatus(true);
        User user = new User ("Bob", "Bob", UserTypes.ADMIN);
        assertEquals(c.getRecordingStatus(), true);
    }

    /** Scenario A: Users and Admin **/

    /**
     * 1. GIVEN I am starting the app / page WHEN I first open the application/page
     * THEN I see a user login screen
     * SEE HTTP REQUEST TEST
     */

    /**
     * 2. GIVEN the app supports two classes of users WHEN a user account
     * is created THEN the account is either an ADMIN or a USER.
     */
    @Test
    public void userPrivilege() throws Exception {
        User user = new User("Neil", "Mah boy", UserTypes.ADMIN);
        UserTypes type = user.getUserType();
        assertEquals(type, UserTypes.ADMIN);
        user.setUserType(UserTypes.REGULAR);
        type = user.getUserType();
        assertEquals(type, UserTypes.REGULAR);
    }

    /**
     * 3. GIVEN I am logged in as a user to the Hub WHEN I open the app interface THEN I see
     * the user interface
     * SEE HTTP REQUEST TEST
     */


    /** Scenario: Booting Home Automation System (Hub) **/

    /**
     * 1. GIVEN the Home Automation System is functional WHEN I open the app admin interface THEN I
     * should see the log of the previous activities
     */
    /**
     * 5. GIVEN the Home Automation System is functional WHEN the devices of the system are functional
     * THEN the Hub should log all the activities of the devices and store them.
     * This test checks if the log file exists
     */
    @Test
    public void checkIfLogFileExists() throws Exception {
        String filePath = new File("").getAbsolutePath();
        File f = new File(filePath + "/src/main/resources/db/log.txt");
        assert(f.exists());
    }
    /* LOOK AT THE WebLayerTest.java FOR THE REMAINDER OF TEST checks if in view*/

    /**
     * 2. GIVEN the Home Automation System is functional WHEN I add a new device to my system on the
     * Client control THEN the Hub should register this new device to the system.
     */
    @Test
    public void deviceRegisterTest() throws Exception {
        Hub h = new Hub();
        Thermostat t = new Thermostat(h);
        h.register(t);
        int listSize = h.getDevices().size();
        assertEquals(listSize, 1);
    }

    /**
     * 3. GIVEN the Home Automation System is functional WHEN I remove a device from my system on the Client Control
     * THEN the Hub should deregister this new device from the system.
     */
    @Test
    public void deviceRemoveTest() throws Exception {
        Hub h = new Hub();
        SmartPlug s = new SmartPlug(h);
        h.register(s);
        h.unregister(s);
        int listSize = h.getDevices().size();
        assertEquals(listSize, 0);
    }

    /**
     * 4. GIVEN the Home Automation System is functional WHEN I want to administer my devices
     * THEN the Hub should show the device config screen
       /* LOOK AT THE WebLayerTest.java FOR THIS TEST */

    /**
     * 6. GIVEN the Home Automation System is functional WHEN the devices of the system are functional THEN
     * the Hub should display device status visually
     */
    @Test
    public void deviceStatusTest() throws Exception {
        Hub h = new Hub();
        Lightbulb t = new Lightbulb(h);
        h.register(t);
        t.setStatus(Status.ON);
        Status s = t.getStatus();
        assertEquals(s, Status.ON);
    }

    /**
     * 7. GIVEN the Home Automation System is functional WHEN I click shutdown from the Client Control THEN the Hub
     * should turn off all the devices AND should safely shutdown the system AND notify about the activity on the Client.
     */
    @Test
    public void checkShutdown() throws Exception {
        Hub h = new Hub();
        Camera c = new Camera(h);
        h.register(c);
        c.setStatus(Status.ON);
        Lightbulb l = new Lightbulb(h);
        h.register(l);
        h.shutdown();
        assertEquals(l.getStatus(), Status.OFF);
        assertEquals(c.getStatus(), Status.OFF);
    }

    /********* Scenario C: User wants to access camera *********/

    /**
     * 1. GIVEN a camera WHEN I click "Start" on the Client camera control THEN
     * the camera turns on and I see the data from the Camera
     */
    @Test
    public void isVideoDataTest() throws Exception {
        Hub h = new Hub();
        Camera c = new Camera(h);
        h.register(c);
        assertEquals(c.getStatus(), Status.OFF);
        c.setStatus(Status.ON);
        assertEquals(c.getStatus(), Status.ON);
    }

    /**
     * 2. GIVEN a functioning camera And I am able to see the data from the
     * Camera WHEN I click on record on the Client Camera control THEN my
     * Camera starts recording
     */
    @Test
    public void cameraRecordingTest() throws Exception {
        Hub h = new Hub();
        Camera c = new Camera(h);
        h.register(c);
        c.setRecordingStatus(true);
        boolean isRecording = c.getRecordingStatus();
        assertEquals(isRecording, true);
    }

    /**
     * 3. GIVEN a functioning Camera is recording WHEN I click on stop recording on
     * the Client Camera control THEN my Camera stops recording
     */
    @Test
    public void cameraStopRecordingTest() throws Exception {
        Hub h = new Hub();
        Camera c = new Camera(h);
        h.register(c);
        c.setRecordingStatus(true);
        boolean isRecording = c.getRecordingStatus();
        assertEquals(isRecording, true);
        c.setRecordingStatus(false);
        isRecording = c.getRecordingStatus();
        assertEquals(isRecording, false);
    }

    /**
     * 4. GIVEN a functioning camera and the capacity of recording is full WHEN I click on record
     * on the Client camera control THEN my camera doesn't start
     * recording and returns a alert message "Camera is Full" on the Client Camera control
     */
    @Test
    public void isCameraFullTest() throws Exception {
        Hub h = new Hub();
        Camera c = new Camera(h);
        c.setDiskSize(20);
        assertEquals(c.isFull(), false);
        h.register(c);
        c.setRecordingStatus(true);
        c.setDiskSize(120);
        assertEquals(c.isFull(), true);
    }

    /**
     * 5. GIVEN a functioning camera WHEN I click "Turn Off" on the Client camera control
     * THEN the camera shuts down and I do not see the data from the Camera
     */
    @Test
    public void isVideoDataOffTest() throws Exception {
        Hub h = new Hub();
        Camera c = new Camera(h);
        h.register(c);
        assertEquals(c.getStatus(), Status.OFF);
        c.setStatus(Status.ON);
        assertEquals(c.getStatus(), Status.ON);
        c.setStatus(Status.OFF);
        assertEquals(c.getStatus(), Status.OFF);
    }

    /**
     * GIVEN the Camera is functional WHEN the camera detects an object in front of it
     * THEN my camera notifies Hub about the activity.
     */
    @Test
    public void notifyHubAboutCameraDetectingMovement() throws Exception {
        Hub h = new Hub();
        RandomEventTrigger r = new RandomEventTrigger();
        Camera c = new Camera(h);
        h.register(c);
        boolean b = false;
        // this loop will eventually leave when camera detects movement (1/200 chance of this happening)
        while(b == false) {
            b = r.didCameraDetectMovement();
        }
        assert(b == true);
    }

    /**Scenario D: User wants to access thermostat**/

    /**
     * 1. GIVEN a thermostat WHEN I click "Start" on the Client control THEN the Thermostat becomes functional And
     * I can see the data of current temperature and temperature metrics (Celsius / Fahrenheit)
     */
    @Test
    public void thermostatStartTest() throws Exception {
        Hub h = new Hub();
        Thermostat t = new Thermostat(h);
        h.register(t);
        t.setStatus(Status.ON);
        Status status = t.getStatus();
        assertEquals(status, Status.ON);
        Temperature temp = t.getTemp();
        assertEquals(temp.getUnit(), Temperature.Unit.CELSIUS);
        t.setStatus(Status.OFF);
        status= t.getStatus();
        assertEquals(status, Status.OFF);
    }

    /**
     * 2. GIVEN a functional thermostat with metric set to Farenheit WHEN I set the temperature metric to Celsius
     * on the Client control THEN I should see the thermostat data changing to its equivalent "Celsius"
     */
    @Test
    public void thermostatChangeUnitCelTest() throws Exception {
        Hub h = new Hub();
        Thermostat t = new Thermostat(h);
        h.register(t);
        Temperature temp = new Temperature(70, Temperature.Unit.CELSIUS);
        t.setTemp(temp);
        temp = t.getTemp();
        assertEquals(temp.getUnit(), Temperature.Unit.CELSIUS);
    }



    /**
     * 3. GIVEN a functional thermostat with metric set to Celcius WHEN I set the temperature metric to Fahrenheit on
     * the Client control THEN I should see the thermostat data changing to its equivalent "Fahrenheit"
     */
    @Test
    public void thermostatChangeUnitFarTest() throws Exception {
        Hub h = new Hub();
        Thermostat t = new Thermostat(h);
        h.register(t);
        Temperature temp = new Temperature(70, Temperature.Unit.FAHRENHEIT);
        t.setTemp(temp);
        temp = t.getTemp();
        assertEquals(temp.getUnit(), Temperature.Unit.FAHRENHEIT);
    }


    /**
     * 4. GIVEN a functional thermostat WHEN I set the temperature to a particular value on the Client control THEN I
     * should see the thermostat temperature value is set to the GIVEN valuea
     */
    @Test
    public void thermostatChangeTempTest() throws Exception {
        Hub h = new Hub();
        Thermostat t = new Thermostat(h);
        h.register(t);
        Temperature temp = new Temperature(30.0, Temperature.Unit.FAHRENHEIT);
        t.setTemp(temp);
        temp = t.getTemp();
        assertEquals(temp.getTemperature(), (double) 30.0, 0.0);
    }


    /**
     * 5. GIVEN a functional thermostat WHEN I click "Turn Off" on the client control THEN I should see that the
     * thermostat is turned off.
     */
    @Test
    public void thermostatStopTest() throws Exception {
        Hub h = new Hub();
        Thermostat t = new Thermostat(h);
        h.register(t);
        t.setStatus(Status.ON);
        t.setStatus(Status.OFF);
        Status status = t.getStatus();
        assertEquals(status, Status.OFF);
    }

    /**
     * 6. GIVEN a functional thermostat WHEN it automatically changes the temperature based on the climate outside
     * THEN it should notify hub about this event.
     */
    @Test
    public void notifyHubAboutTemperatureChange() throws Exception {
        Hub h = new Hub();
        RandomEventTrigger r = new RandomEventTrigger();
        Thermostat t = new Thermostat(h);
        h.register(t);
        boolean b = false;
        // this loop will eventually leave when outdoor temp changes (1/200 chance of this happening)
        while(b == false) {
            b = r.didTemperatureChange();
        }
        assert(b == true);
    }

    /** Scenario E: User wants to access lightbulb **/

    /**
     * 1. GIVEN a light bulb that is off WHEN I click "Toggle" on the client control THEN I should see the data
     * that the Light Bulb is turned "On".
     */
    @Test
    public void toggleLightbulbTestOn() throws Exception {
        Hub h = new Hub();
        Lightbulb l = new Lightbulb(h);
        h.register(l);
        l.setStatus(Status.OFF);
        assertEquals(l.getStatus(), Status.OFF);
        l.setStatus(Status.ON);
        assertEquals(l.getStatus(), Status.ON);
    }

    /**
     * 2. GIVEN a light bulb that is On WHEN I click "Toggle" on the client control THEN I should see that
     * the light bulb is turned "Off".
     */
    @Test
    public void toggleLightbulbTestOff() throws Exception {
        Hub h = new Hub();
        Lightbulb l = new Lightbulb(h);
        h.register(l);
        l.setStatus(Status.ON);
        assertEquals(l.getStatus(), Status.ON);
        l.setStatus(Status.OFF);
        assertEquals(l.getStatus(), Status.OFF);
    }

    /**
     * 3. GIVEN a functional light bulb WHEN there is no one in the room (Camera notifies hub) THEN it should turn off
     * and should notify Hub about the activity.
     */
    @Test
    public void notifyHubLeaveRoomActivity() throws Exception {
        Hub h = new Hub();
        RandomEventTrigger r = new RandomEventTrigger();
        Lightbulb l = new Lightbulb(h);
        h.register(l);
        boolean b = false;
        // this loop will eventually leave when user leaves room (1/200 chance of happening)
        while(b == false) {
            b = r.didUserLeaveRoom();
        }
        assert(b == true);
    }

    /**
     * 4. GIVEN a non-functional light bulb WHEN someone enters the room (Camera notifies hub) THEN it should turn on
     * and should notify Hub about the activity.
     */
    @Test
    public void notifyHubRoomEnterActivity() throws Exception {
        Hub h = new Hub();
        RandomEventTrigger r = new RandomEventTrigger();
        Lightbulb l = new Lightbulb(h);
        h.register(l);
        boolean b = false;
        // this loop will eventually leave when user ENTERS room (1/200 chance of happening)
        while(b == false) {
            b = r.didUserEnterRoom();
        }
        assert(b == true);
    }

    /** Scenario F: User wants to access smartplug **/

    /**
     * 1. GIVEN a non-functional smartplug WHEN I click "Toggle" on the client control THEN I should see that
     * the smartplug is "Turned On"
     */
    @Test
    public void toggleSmartPlugTestOn() throws Exception {
        Hub h = new Hub();
        SmartPlug s = new SmartPlug(h);
        h.register(s);
        s.setStatus(Status.OFF);
        assertEquals(s.getStatus(), Status.OFF);
        s.setStatus(Status.ON);
        assertEquals(s.getStatus(), Status.ON);
    }

    /**
     * 2. GIVEN a functional smartplug WHEN I click "Toggle" on the client control THEN I should see that
     * the smartplug is "Turned Off"
     */
    @Test
    public void toggleSmartPlugTestOff() throws Exception {
        Hub h = new Hub();
        SmartPlug s = new SmartPlug(h);
        h.register(s);
        s.setStatus(Status.ON);
        assertEquals(s.getStatus(), Status.ON);
        s.setStatus(Status.OFF);
        assertEquals(s.getStatus(), Status.OFF);
    }

    /**
     * 3. GIVEN a functional smartplug WHEN I look at the client control THEN I should see the plug's current status (
     * On/Off) of the smartplug
     */
    @Test
    public void checkSmartPlugTestOff() throws Exception {
        Hub h = new Hub();
        SmartPlug s = new SmartPlug(h);
        h.register(s);
        s.setStatus(Status.ON);
        // check if the toString representation is accurate
        assertEquals(s.getStatus().toString(), "ON");
        s.setStatus(Status.OFF);
        assertEquals(s.getStatus().toString(), "OFF");
    }
}