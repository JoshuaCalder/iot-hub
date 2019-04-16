package hello.Devices;

import hello.Status;
import hello.Mediator;
import java.util.UUID;

public class Camera extends Device{
    private boolean isRecording = false;
    private int diskSize = 0;
    private final Mediator aMed;

    public Camera(Mediator pMed) {
        this.aMed = pMed;
        this.setStatus(Status.OFF);
        this.setIdentifier(UUID.randomUUID());
    }

    public Camera(Mediator pMed, UUID uuid, Status status) {
        this.aMed = pMed;
        this.setStatus(status);
        this.setIdentifier(uuid);
    }

    public boolean getRecordingStatus() { return this.isRecording; }

    public void setRecordingStatus(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public void setDiskSize(int diskSize) { this.diskSize = diskSize; }

    public int getDiskSize() { return this.diskSize; }

    public boolean isFull() { return !(this.diskSize < 100); }

    @Override
    public String toString() {
        return "Camera id: " + super.getIdentifier().toString();
    }

}