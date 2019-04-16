package hello.Devices;

import hello.Status;
import hello.Mediator;

public class Temperature {

    public enum Unit {
        CELSIUS, FAHRENHEIT
    }

    private Unit unit = Unit.CELSIUS;
    private double temperature = 0.0;

    public Temperature(double temperature, Unit unit) throws TemperatureOutofBoundsException {
        if (temperature > 1000) {
            throw new TemperatureOutofBoundsException("Absurd temperature!");
        }

        this.temperature = temperature;
        this.unit = unit;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(double temperature) {
        double roundedTemp = Math.round(temperature * 100.0)/100.0;
        this.temperature = roundedTemp;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public class TemperatureOutofBoundsException extends Exception {
        public TemperatureOutofBoundsException(String s) {
            super(s);
        }
    }
}