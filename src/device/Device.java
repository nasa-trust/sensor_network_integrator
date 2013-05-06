/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package device;

import java.util.ArrayList;

/**
 *
 * @author Shrikant
 */
public class Device {
    
    private String guid;
    private String uri;
    private String printName;
    private Location location;
    private ArrayList<Sensor> sensorList;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public ArrayList<Sensor> getSensorList() {
        return sensorList;
    }

    public void setSensorList(ArrayList<Sensor> sensorList) {
        this.sensorList = sensorList;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    } 
    
}
