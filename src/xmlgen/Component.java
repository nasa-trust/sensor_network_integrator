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
@XmlRootElement(name="component")
public class Component {
   private String componentType;
   private String name;
   private float xposition;
   private float yposition;

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getXposition() {
        return xposition;
    }

    public void setXposition(float xposition) {
        this.xposition = xposition;
    }

    public float getYposition() {
        return yposition;
    }

    public void setYposition(float yposition) {
        this.yposition = yposition;
    }
    
    
    

   
}
