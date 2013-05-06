/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgen;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Shrikant
 */
@XmlRootElement(name="systems")
public class Systems {
    private List<System> system;

    public List<System> getSystem() {
        return system;
    }

    public void setSystem(List<System> system) {
        this.system = system;
    }
    
}
