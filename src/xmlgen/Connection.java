/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgen;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Shrikant
 */
@XmlRootElement(name="connection")
public class Connection {
    private String c1;
    private String c2;

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }
    
}
