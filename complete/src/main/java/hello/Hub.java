package hello;

import hello.Devices.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.lang.String;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.logging.FileHandler;
import java.util.logging.*;
import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class Hub extends Device implements Mediator {

    private HashMap<UUID, Device> aDevices = new HashMap<UUID, Device>();
    private HashMap<UUID, Client> aClients = new HashMap<UUID, Client>();
    private HashMap<String, User> aUsers = new HashMap<String, User>();
    private Logger logger = LoggerFactory.getLogger(Hub.class);
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void register(Device pDevice) throws HubRegistrationException {
        if (!aDevices.containsKey(pDevice.getIdentifier())) {
            aDevices.put(pDevice.getIdentifier(), pDevice);
        } else {
            throw new HubRegistrationException(pDevice + " was already registered");
        }
    }

    @Override
    public void register(Client pClient) throws HubRegistrationException {
        if (!aClients.containsKey(pClient.getIdentifier())) {
            aClients.put(pClient.getIdentifier(), pClient);
        } else {
            throw new HubRegistrationException(pClient + " was already registered");
        }
    }

    public void register(User pUser) throws HubRegistrationException {
        if (!aUsers.containsKey(pUser.getUserName())) {
            aUsers.put(pUser.getUserName(), pUser);
        } else {
            throw new HubRegistrationException(pUser + " was already registered"); /* Must catch and update view */
        }
    }

    public void registerExistingUser(User pUser) throws HubRegistrationException {
        if (!aUsers.containsKey(pUser.getUserName())) {
            aUsers.put(pUser.getUserName(), pUser);
        } else {
            throw new HubRegistrationException(pUser + " was already registered"); /* Must catch and update view */
        }
    }

    public void unregister(String userName) throws HubRegistrationException {
        if (!aUsers.containsKey(userName)) {
            log("Unknown User unregister");
            throw new HubRegistrationException("User does not exists!");
        }
        aUsers.remove(userName);
        log("User removed " + userName);
    }

    public User userValidation(User pUser) throws WrongPasswordException, NoSuchUserException {
        if (!aUsers.containsKey(pUser.getUserName())) {
            throw new NoSuchUserException(pUser.getUserName() + " is not a registered user.");
        } else if (aUsers.get(pUser.getUserName()).getPassword().compareTo(pUser.getPassword()) != 0) {
            throw new WrongPasswordException("Wrong password for user " + pUser.getUserName());
        } else {
            return new User(aUsers.get(pUser.getUserName()));
        }
    }

    public void changeLogVis (String userName)throws HubRegistrationException {
        if (!aUsers.containsKey(userName)) {
            log("Unknown User changeLogVis");
            throw new HubRegistrationException("User does not exists!");
        }
        aUsers.get(userName).setCanSeeLog();
        log("Changed log visibility for " + userName);
    }

    public boolean returnLogVis (String userName) {
        return aUsers.get(userName).isCanSeeLog();
    }

    @Override
    public void unregister(Device device) throws HubRegistrationException {
        if (!aDevices.containsKey(device.getIdentifier())) {
            log("Unknown Device unregister");
            throw new HubRegistrationException("Device does not exists!");
        }
        aDevices.remove(device.getIdentifier());
        log("Device removed " + device);
    }

    @Override
    public void unregister(Client client) throws HubRegistrationException {
        if (!aClients.containsKey(client.getIdentifier())) {
            throw new HubRegistrationException("Client does not exists!");
        }
        aClients.remove(client.getIdentifier());
    }

    /**
     * Logging. Use SLF4J to write all message traffic to the log file.
     *
     * @param logMsg
     */
    public void log(String logMsg) {
        logger.info(logMsg);
    }

    /**
     * Alerts are events that happen at the Device level. They send the alert to the hUb, which
     * redistributes to the clients
     *
     * @param pMessage
     */
    @Override
    public void alert(Device pDevice, String pMessage) {

        // initialize the map
        JSONObject jsonMessage = new JSONMessaging(pDevice, pMessage).invoke();

        // ordinarily, we would have logic for which clients to notify
        notifyClients(jsonMessage);
        log("ALERT msg: " + pMessage + " - from Device " + pDevice.toString());
    }

    private void notifyClients(JSONObject pMsg) {
        for (Client c : aClients.values()) {
            c.notify(pMsg);
            log("Notified: " + c.toString());
        }
    }

    public Map<UUID, Device> getDevices() {
        return new HashMap<UUID, Device>(this.aDevices);
    }

    public Device getDevice(UUID uuid) {
        return this.aDevices.get(uuid);
    }

    public Map<String, User> getUsers() {
        return new HashMap<String, User>(this.aUsers);
    }

    public User getUser(UUID uuid) {
        return this.aUsers.get(uuid);
    }

    public void shutdown() {
        for (Map.Entry<UUID, Device> entry : aDevices.entrySet()) {
            entry.getValue().turnOffDevice();
        }
    }

}