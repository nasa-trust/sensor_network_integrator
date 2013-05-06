/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;

/**
 *
 * @author Shrikant
 */
public class SensorReading {

    private double digitalTemp;
    private double motion;
    private double pressure;
    private double light;
    private double humidity;
    private double bat;
    private double audioP2P;
    private int Id;
    private double gpioState;
    private double accX;
    private double accY;
    private double accZ;
    private double timestamp;
    private double temp;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public double getAccZ() {
        return accZ;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public double getAudioP2P() {
        return audioP2P;
    }

    public void setAudioP2P(double audioP2P) {
        this.audioP2P = audioP2P;
    }

    public double getBat() {
        return bat;
    }

    public void setBat(double bat) {
        this.bat = bat;
    }

    public double getDigitalTemp() {
        return digitalTemp;
    }

    public void setDigitalTemp(double digitalTemp) {
        this.digitalTemp = digitalTemp;
    }

    public double getGpioState() {
        return gpioState;
    }

    public void setGpioState(double gpioState) {
        this.gpioState = gpioState;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public double getMotion() {
        return motion;
    }

    public void setMotion(double motion) {
        this.motion = motion;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
}
