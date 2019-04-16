package hello.Devices;

import hello.Status;
import hello.Mediator;
import java.util.UUID;

public class SmartPlug extends Device {
    private final Mediator aMed;

    public SmartPlug(Mediator pMed) {
        super();
        aMed = pMed;
        this.setStatus(Status.OFF);
        this.setIdentifier(UUID.randomUUID());
    }

    public SmartPlug(Mediator pMed, UUID uuid, Status status) {
        super();
        aMed = pMed;
        this.setStatus(status);
        this.setIdentifier(uuid);
    }

    @Override
    public String toString() {
        return "SmartPlug id: " + super.getIdentifier().toString();
    }
    //comment
}

