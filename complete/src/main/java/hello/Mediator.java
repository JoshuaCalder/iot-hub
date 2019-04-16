package hello;

import hello.Devices.Device;

public interface Mediator {

    public void unregister(Device device) throws HubRegistrationException;

    public void unregister(Client client) throws HubRegistrationException;

    public void register(Device pDevice) throws HubRegistrationException;

    public void register(Client pClient) throws HubRegistrationException;

    public void alert(Device pDevice, String pMessage);
}