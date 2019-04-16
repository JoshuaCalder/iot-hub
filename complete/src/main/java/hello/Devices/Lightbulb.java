package hello.Devices;

import hello.Status;
import hello.Mediator;
import java.util.UUID;

public class Lightbulb extends Device {
    private final Mediator aMed;

    public Lightbulb(Mediator pMed) {
        super();
        aMed = pMed;
        this.setStatus(Status.OFF);
        this.setIdentifier(UUID.randomUUID());
    }

    public Lightbulb(Mediator pMed, UUID uuid, Status status) {
        super();
        aMed = pMed;
        this.setStatus(status);
        this.setIdentifier(uuid);
    }

    @Override
    public String toString() {
        return "Lightbulb id: " + super.getIdentifier().toString();
    }
}