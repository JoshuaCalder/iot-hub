/**
 * This class generates random events for functional devices
 * The probability of event outcome:
 * 1/200 for thermostat detecting temperature change
 * 2/200 for camera detecting movement
 * To see these events occuring more frequently, increase the randomNum return range.
 */

package hello;

import java.util.Random;

public class RandomEventTrigger {
    // Checks if funcitonal camera has detected movement
    public boolean didCameraDetectMovement() {
        int rndNum = generateRandomInt(200);
        return (rndNum < 2);
    }

    // Checks if outdoor temperature has changed
    public boolean didTemperatureChange() {
        int rndNum = generateRandomInt(200);
        return (rndNum == 1);
    }

    // Generates a new random outdoor temperature
    public double newOutdoorTemperature(double currTemp) {
        Random rand = new Random();
        double minRange = currTemp -= 1.0;
        double maxRange = currTemp += 1.0;
        return (minRange + (maxRange - minRange) * rand.nextDouble());
    }

    public boolean didUserEnterRoom() {
        int rndNum = generateRandomInt(200);
        return (rndNum == 1);
    }

    public boolean didUserLeaveRoom() {
        int rndNum = generateRandomInt(200);
        return (rndNum == 1);
    }

    private int generateRandomInt(int maxRange) {
        Random rand = new Random();
        return rand.nextInt(100) + 1;
    }
}