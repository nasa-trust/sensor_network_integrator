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
@XmlRootElement(name="system")
public class System {
    
    private String systemName;
    private String description;
    private Components components;
    private Connections connections;

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public Connections getConnections() {
        return connections;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
    }

   

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
    
}
