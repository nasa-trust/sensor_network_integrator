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
@XmlRootElement(name="connections")
public class Connections {
    private List<Connection> connection;

    public List<Connection> getConnection() {
        return connection;
    }

    public void setConnection(List<Connection> connection) {
        this.connection = connection;
    }
      
    
}
