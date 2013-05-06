/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package device;

/**
 *
 * @author Shrikant
 */
public class Location {
    
    private String printName;
    private String lat;
    private String lon;
    private String alt;
    private String format;

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }
    
}
