package hello.Services;

import hello.*;
import hello.Devices.*;

import org.springframework.stereotype.Service;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ThermostatService extends AbstractService {

    public void toggle(Thermostat t) {
        Status currStatus = t.getStatus();
        if (currStatus == Status.OFF) {
            t.setStatus(Status.ON);
            writeLog("Thermostat " + t.getIdentifier() + " turned ON");
        } else {
            t.setStatus(Status.OFF);
            writeLog("Thermostat " + t.getIdentifier() + " turned OFF");
        }
        updateDatabase();
    }

    public void convertUnit(Thermostat t) {
        Temperature currTemp = t.getTemp();
        double temp = currTemp.getTemperature();
        Temperature.Unit unit = currTemp.getUnit();

        if (unit == Temperature.Unit.CELSIUS) {
            currTemp.setTemperature((temp * 1.800) + 32.00);
            currTemp.setUnit(Temperature.Unit.FAHRENHEIT);
        } else {
            currTemp.setTemperature(((temp - 32.00) * 5.00) / 9.00);
            currTemp.setUnit(Temperature.Unit.CELSIUS);
        }
    }

    public void increaseTemp(Thermostat t) {
        double newTemp = t.getTemp().getTemperature() + 1.00;
        t.getTemp().setTemperature(newTemp);
    }

    public void decreaseTemp(Thermostat t) {
        double newTemp = t.getTemp().getTemperature() - 1.00;
        t.getTemp().setTemperature(newTemp);
    }
}