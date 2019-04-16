package hello.Devices;

import hello.Status;
import hello.Mediator;
import java.util.UUID;

public class Thermostat extends Device {
    private Temperature setPoint;
    private final Mediator aMed;
    private final double DEFAULT_TEMP = 25.0;
    private final Temperature.Unit DEFAULT_UNIT = Temperature.Unit.CELSIUS;

    public Thermostat(Mediator pMed) throws Temperature.TemperatureOutofBoundsException {
        super();
        aMed = pMed;
        this.setStatus(Status.OFF);
        this.setIdentifier(UUID.randomUUID());
        this.setPoint = new Temperature(DEFAULT_TEMP, DEFAULT_UNIT);
    }

    public Thermostat(Mediator pMed, UUID uuid, Status status) throws Temperature.TemperatureOutofBoundsException {
        super();
        aMed = pMed;
        this.setStatus(status);
        this.setIdentifier(uuid);
        this.setPoint = new Temperature(DEFAULT_TEMP, DEFAULT_UNIT);
    }

    public void setTemp(Temperature temperature) {
        this.setPoint = temperature;
    }

    public Temperature getTemp() {
        return this.setPoint;
    }

    @Override
    public String toString() {
        return "Thermostat id: " + super.getIdentifier().toString();
    }
}