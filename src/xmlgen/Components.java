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
@XmlRootElement(name = "components")
public class Components {

    private List<Component> component;

    public List<Component> getComponent() {
        return component;
    }

    public void setComponent(List<Component> component) {
        this.component = component;
    }
}
