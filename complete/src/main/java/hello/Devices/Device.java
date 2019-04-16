package hello.Devices;

import hello.Status;
import hello.Mediator;
import java.util.UUID;

public abstract class Device {

    private UUID aUuid;
    private Status aStatus; // This can't be NULL!

    public UUID getIdentifier() {
        return aUuid;
    }

    public void setIdentifier(UUID uuid) { this.aUuid = uuid; }

    public Status getStatus() {
        // Since the status can't be NULL, then check IF NULL and IF return dummy
        // status.
        return aStatus == null ? Status.NOT_AVAILABLE : aStatus;
    }

    public void setStatus(Status status) {
        this.aStatus = status;
    }

    public void turnOffDevice() {
        this.aStatus = Status.OFF;
    }

    @Override
    public String toString() {
        return aUuid.toString();
    }



}