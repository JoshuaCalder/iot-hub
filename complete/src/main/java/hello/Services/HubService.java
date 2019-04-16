package hello.Services;

import hello.*;
import hello.Devices.*;
import hello.RandomEventTrigger;

import org.springframework.stereotype.Service;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.*;
import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.Enumeration;

/* imports for JSON */
import java.util.UUID;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.FileReader;

@Service
public class HubService extends AbstractService {
    RandomEventTrigger randomEvent;

    @Inject
    private Hub h;

    /**
     * This gets triggered at application launch (Hub startup)
     * Creates a new hub, loads previous devices & log
     */
    @Autowired
    void HubService() {
        Hub h = new Hub();
        randomEvent = new RandomEventTrigger();
        loadUsers();
        loadHubDevices();
    }

    public Hub getH() {
        return h;
    }

    public UUID addCamera() throws HubRegistrationException {
        Camera c = new Camera(h);
        h.register(c);
        updateDatabase();
        return c.getIdentifier();
    }

    public UUID addSmartPlug() throws HubRegistrationException{
        SmartPlug s = new SmartPlug(h);
        h.register(s);
        updateDatabase();
        return s.getIdentifier();
    }

    public UUID addThermostat() throws HubRegistrationException, Temperature.TemperatureOutofBoundsException {
        Thermostat t = new Thermostat(h);
        h.register(t);
        updateDatabase();
        return t.getIdentifier();
    }

    public UUID addLightbulb() throws HubRegistrationException {
        Lightbulb l = new Lightbulb(h);
        h.register(l);
        updateDatabase();
        return l.getIdentifier();
    }

    public void unregisterDevice(Device device) throws HubRegistrationException
    {
        h.unregister(device);
        updateDatabase();
    }

    public void registerUser(User user) throws HubRegistrationException
    {
        h.register(user);
        writeUsers();
    }

    public void unregisterUser(String userName) throws HubRegistrationException
    {
        h.unregister(userName);
        writeLog("User <" + userName + "> was removed by administrator <" + h.getCurrentUser().getUserName() + ">");
        writeUsers();
    }

    public void registerExistingUser(User user) throws HubRegistrationException {
        h.register(user);
    }

    public User validateUser(User user) throws WrongPasswordException, NoSuchUserException {
        return h.userValidation(user);
    }

    public User getCurrentUserFromHub() {
        return h.getCurrentUser();
    }

    public void setCurrentUserToHub(User currentUser) {
        h.setCurrentUser(currentUser);
    }

    public boolean isCurrentUserAdmin() {
        if (h.getCurrentUser().getUserType() == UserTypes.ADMIN)
            return true;
        else
            return false;
    }

    public void setLogVis(String userName) throws HubRegistrationException {
        h.changeLogVis(userName);
    }

    public boolean isCanSeeLog(String userName) {
        return h.returnLogVis(userName);
    }

    public String getCurrentUserName() {
        return h.getCurrentUser().getUserName();
    }

    public Map<String, User> returnUsers() {
        return h.getUsers();
    }

    public void shutdown() {
        Map<UUID, Device> deviceList = h.getDevices();
        for (Map.Entry<UUID, Device> entry : deviceList.entrySet()) {
            entry.getValue().turnOffDevice();
        }
        updateDatabase();
        writeLog("SHUTDOWN: All devices turned off.");
    }

    /**
     * Iterates through devices and checks if the functional ones have detected events
     * (ie. camera has detected movement / thermostat detects temp change)
     * Writes detected activity to log and notifies the hub
     * @return String with message to alert hub with
     */
    public String getRandomEvents() {
        String response = "";
        Map<UUID, Device> deviceList = h.getDevices();

        for (Map.Entry<UUID, Device> entry : deviceList.entrySet()) {
            String deviceType = entry.getValue().getClass().getSimpleName();
            Status deviceStatus = entry.getValue().getStatus();
            UUID deviceUUID = entry.getValue().getIdentifier();

            if (deviceType.equals("Camera") && deviceStatus == Status.ON && randomEvent.didCameraDetectMovement()) {
                writeLog("Movement detected by camera " + deviceUUID);
                response += "Movement detected by camera " + deviceUUID + "\n";
            } else if (deviceType.equals("Thermostat") && deviceStatus == Status.ON && randomEvent.didTemperatureChange()) {
                writeLog("Outside temperature change detected by thermostat " + deviceUUID);
                response += "Outside temperature change detected by thermostat " + deviceUUID + "\n";
            } else if (deviceType.equals("Lightbulb")){
                if (deviceStatus == Status.OFF && randomEvent.didUserEnterRoom()) {
                    entry.getValue().setStatus(Status.ON);
                    writeLog("Hub detected user has entered room. Turning on Lightbulb " + deviceUUID);
                    response += "Hub detected user has entered room. Turning on Lightbulb " + deviceUUID + "\n";
                } else if (deviceStatus == Status.ON && randomEvent.didUserLeaveRoom()) {
                    entry.getValue().setStatus(Status.OFF);
                    writeLog("Hub detected user has left room. Turning off Lightbulb " + deviceUUID);
                    response += "Hub detected user has left room. Turning off Lightbulb " + deviceUUID + "\n";
                }
            }
        }
        return response;
    }

    public Map<UUID, Device> returnDevices() {
        return h.getDevices();
    }

    public Device getDevice(UUID uuid) {
        return h.getDevice(uuid);
    }

    // Registers an existing device that was saved in the data
    public void registerExistingDevice(String device, UUID uuid, Status status) throws HubRegistrationException, Temperature.TemperatureOutofBoundsException {
        if (device.equals("Camera")) {
            Camera c = new Camera(h, uuid, status);
            h.register(c);
        } else if (device.equals("SmartPlug")) {
            SmartPlug s = new SmartPlug(h, uuid, status);
            h.register(s);
        } else if (device.equals("Thermostat")) {
            Thermostat t = new Thermostat(h, uuid, status);
            h.register(t);
        } else if (device.equals("Lightbulb")) {
            Lightbulb l = new Lightbulb(h, uuid, status);
            h.register(l);
        }
    }

    // Iterates through json file and adds all devices. Includes device uuid, status and type
    private void loadHubDevices() {
        try {
            JSONParser parser = new JSONParser();
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            JSONArray array = (JSONArray) parser.parse(new FileReader(classLoader.getResource("db/database.json").getFile()));
            Iterator<Object> iterator = array.iterator();
            while(iterator.hasNext()) {
                String deviceType = null;
                UUID uuid = null;
                Status status = null;
                JSONObject jsonObject = (JSONObject)iterator.next();
                for (Object key : jsonObject.keySet()){
                    if (key.equals("Type")) {
                        deviceType = (String)jsonObject.get(key);
                    }
                    if (key.equals("UUID")) {
                        uuid = UUID.fromString((String)jsonObject.get(key));
                    }
                    if (key.equals("Status")) {
                        status = Status.valueOf((String)jsonObject.get(key));
                    }
                }
                assert(deviceType != null && uuid != null && status != null);
                registerExistingDevice(deviceType, uuid, status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        try {
            JSONParser parser = new JSONParser();
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            JSONArray array = (JSONArray) parser.parse(new FileReader(classLoader.getResource("db/users.json").getFile()));
            Iterator<Object> iterator = array.iterator();
            while(iterator.hasNext()) {
                String username = null;
                String password = null;
                UserTypes userType = null;
                JSONObject jsonObject = (JSONObject)iterator.next();
                for (Object key : jsonObject.keySet()){
                    if (key.equals("Username")) {
                        username = (String)jsonObject.get(key);
                    }
                    if (key.equals("Password")) {
                        password = (String)jsonObject.get(key);
                    }
                    if (key.equals("Usertype")) {
                        userType = UserTypes.valueOf((String)jsonObject.get(key));
                    }
                }
                assert(username != null && password != null && userType != null);
                User u = new User(username, password, userType);
                registerExistingUser(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeUsers() {
        Map<String, User> users = h.getUsers();
        String filePath = new File("").getAbsolutePath();
        FileWriter f = null;
        JSONObject json;
        try {
            f = new FileWriter(filePath + "/src/main/resources/db/users.json");
            f.write("[\n");
            for (Map.Entry<String, User> entry : users.entrySet()) {
                String username = entry.getKey();
                String password = entry.getValue().getPassword();
                String usertype = entry.getValue().getUserType().toString();
                json = new JSONObject();
                json.put("Username", username);
                json.put("Password", password);
                json.put("Usertype", usertype);
                f.write(json.toString());
                f.write(",\n");
            }
            json = new JSONObject();
            f.write("]");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (f != null) {
                    f.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getLog() {
        String filePath = new File("").getAbsolutePath();
        try {
            List<String> input = Files.readAllLines(Paths.get(filePath + "/src/main/resources/db/log.txt"), StandardCharsets.UTF_8);
            return input.toArray(new String[input.size()]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[0];
    }
}