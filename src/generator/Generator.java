/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import device.Device;
import device.Location;
import device.Sensor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import xmlgen.Component;
import xmlgen.Components;
import xmlgen.Connection;
import xmlgen.Connections;
import xmlgen.Systems;

/**
 *
 * @author Shrikant
 */
public class Generator {

    private static float xpos[];
    private static float ypos[];
    private Properties properties;
    private static String inputJsonURL;
    private static String noNamespaceSchemaLocation;
    private static String outputXMLLocation;
    private static String systemDescription;
    private static String systemName;
    private static String urlForSensorReadings;
    private static long startTime;
    private static long endTime;
    private int totalSensorCount = 0;
    private static String textOuputURL;

    public Generator() throws PropertiesFileNotFoundException {
        properties = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("file_properties.properties");
        try {
            properties.load(inputStream);
            inputJsonURL = properties.getProperty("input_json_url");
            noNamespaceSchemaLocation = properties.getProperty("noNamespaceSchemaLocation");
            systemDescription = properties.getProperty("system_description_in_xml");
            systemName = properties.getProperty("system_name_in_xml");
            outputXMLLocation = properties.getProperty("output_xml_location");
            urlForSensorReadings = properties.getProperty("url_for_sensor_readings");
            startTime = Long.parseLong(properties.getProperty("start_time"));
            endTime = Long.parseLong(properties.getProperty("end_time"));
            textOuputURL = properties.getProperty("text_file_location");


        } catch (IOException ex) {
            throw new PropertiesFileNotFoundException("Please add the 'Properties' folder to the class path. Kindly also check that the folder has the file titled 'file_properties.properties'");
        }

    }

    public static String connectToSOLR(String inputJsonURL) {
        String respContent = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet http = new HttpGet(inputJsonURL);
            HttpResponse response = httpclient.execute(http);
            HttpEntity entity = response.getEntity();

            respContent = EntityUtils.toString(entity);
            System.out.println(respContent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return respContent;
    }

    private ArrayList<Device> parseJSONAndBuildDeviceList(String jsonString) throws ParseException {
        JSONParser jSONParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jSONParser.parse(jsonString);
        ArrayList<Device> deviceList = new ArrayList<Device>();
        for (Object object : jsonArray) {
            Device device = new Device();
            JSONObject deviceJSON = (JSONObject) object;
            device.setGuid(deviceJSON.get("guid") != null ? deviceJSON.get("guid").toString() : null);
            device.setUri(deviceJSON.get("uri") != null ? deviceJSON.get("uri").toString() : null);
            device.setPrintName(deviceJSON.get("print_name") != null ? deviceJSON.get("print_name").toString() : null);
            Location location = new Location();
            JSONObject locationJSONObj = (JSONObject) deviceJSON.get("location");
            location.setPrintName(locationJSONObj.get("print_name") != null ? locationJSONObj.get("print_name").toString() : null);
            location.setLat(locationJSONObj.get("lat") != null ? locationJSONObj.get("lat").toString() : null);
            location.setLon(locationJSONObj.get("lon") != null ? locationJSONObj.get("lon").toString() : null);
            location.setAlt(locationJSONObj.get("alt") != null ? locationJSONObj.get("alt").toString() : null);
            location.setFormat(locationJSONObj.get("format") != null ? locationJSONObj.get("format").toString() : null);
            device.setLocation(location);
            ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
            JSONArray sensorJSONArray = (JSONArray) deviceJSON.get("sensors");

            for (Object sensorObj : sensorJSONArray) {
                JSONObject sensorJSON = (JSONObject) sensorObj;
                Sensor sensor = new Sensor();
                String key = (String) sensorJSON.keySet().iterator().next();
                sensor.setSensorId(key);
                sensor.setSensorType(sensorJSON.get(key).toString());
                sensorList.add(sensor);
            }
            device.setSensorList(sensorList);
            deviceList.add(device);
        }

        return deviceList;
    }

    public void xmlWriter(Systems systems, String filename) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(Systems.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty("jaxb.noNamespaceSchemaLocation", noNamespaceSchemaLocation);
        File file = null;
        file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        m.marshal(systems, file);
    }

    private void buildObjectForXMLGeneration(ArrayList<Device> deviceList) throws JAXBException, IOException {
        Systems systems = new Systems();
        xmlgen.System system = new xmlgen.System();
        system.setDescription(systemDescription);
        system.setSystemName(systemName);
        Components components = new Components();
        Connections connections = new Connections();
        int counter = 1;
        int xcounter = 0;
        int ycounter = 0;
        generateGridPositions(deviceList.size());


        List<Component> componentList = new ArrayList<Component>();
        List<Connection> connectionList = new ArrayList<Connection>();

        for (Device device : deviceList) {

            String deviceName = (device.getLocation().getPrintName() != null ? device.getLocation().getPrintName() + "." : "") + (device.getPrintName() != null ? device.getPrintName() : "device" + counter++);
            Component component = new Component();
            component.setComponentType("device");
            component.setName(deviceName);
            component.setXposition(xpos[xcounter]);
            component.setYposition(ypos[ycounter]);
            componentList.add(component);
//            Connection connection = new Connection();
//            connection.setC1("device");
//            connection.setC2("device" + counter);
//            connectionList.add(connection);
            ArrayList<Sensor> sensorList = device.getSensorList();
            int sensorCount = 0;
            for (Sensor sensor : sensorList) {
                component = new Component();
                component.setComponentType("sensor");
                component.setName(deviceName + "." + sensor.getSensorType());
                component.setXposition(xpos[xcounter] + sensorCount);
                component.setYposition(ypos[ycounter] + sensorCount);
                componentList.add(component);
                Connection connection = new Connection();
                connection.setC1(deviceName);
                connection.setC2(deviceName + "." + sensor.getSensorType());
                connectionList.add(connection);
                sensorCount++;
            }

            totalSensorCount += sensorCount;

            ycounter++;
            if (ycounter > ypos.length - 1) {
                ycounter = 0;
                xcounter++;
            }
        }
        System.out.println(totalSensorCount);
        components.setComponent(componentList);
        connections.setConnection(connectionList);
        system.setComponents(components);
        system.setConnections(connections);
        ArrayList<xmlgen.System> systemList = new ArrayList<xmlgen.System>();
        systemList.add(system);
        systems.setSystem(systemList);
        xmlWriter(systems, outputXMLLocation);

    }

    private void generateGridPositions(int size) {
        int i = 1;
        while (i * i < size) {
            i++;
        }
        xpos = new float[i];
        ypos = new float[i];
        for (int j = 0; j < i; j++) {
            xpos[j] = 25 * (j + 1);
            ypos[j] = 25 * (j + 1);
        }
    }

//    private ArrayList<SensorReading> parseJSONForSensor(String deviceId, long startTime, long endTime) throws ParseException {
//        String jsonString = connectToSOLR(urlForSensorReadings + deviceId + "?start_time=" + startTime + "&end_time=" + endTime);
//        JSONParser jSONParser = new JSONParser();
//        JSONArray jsonArray = (JSONArray) jSONParser.parse(jsonString);
//        ArrayList<SensorReading> sensorList = new ArrayList<SensorReading>();
//        for (Object object : jsonArray) {
//            SensorReading sensorReading = new SensorReading();
//            JSONObject deviceJSON = (JSONObject) object;
//            sensorReading.setAccX(deviceJSON.get("acc_x") != null ? Double.parseDouble(deviceJSON.get("acc_x").toString()) : null);
//            sensorReading.setAccY(deviceJSON.get("acc_y") != null ? Double.parseDouble(deviceJSON.get("acc_y").toString()) : null);
//            sensorReading.setAccZ(deviceJSON.get("acc_z") != null ? Double.parseDouble(deviceJSON.get("acc_z").toString()) : null);
//            sensorReading.setTimestamp(deviceJSON.get("timestamp") != null ? Double.parseDouble(deviceJSON.get("timestamp").toString()) : null);
//            sensorReading.setTemp(deviceJSON.get("temp") != null ? Double.parseDouble(deviceJSON.get("temp").toString()) : null);
//            sensorReading.setMotion(deviceJSON.get("motion") != null ? Double.parseDouble(deviceJSON.get("motion").toString()) : null);
//            sensorReading.setAudioP2P(deviceJSON.get("audio_p2p") != null ? Double.parseDouble(deviceJSON.get("audio_p2p").toString()) : null);
//            sensorReading.setBat(deviceJSON.get("bat") != null ? Double.parseDouble(deviceJSON.get("bat").toString()) : null);
//            sensorReading.setDigitalTemp(deviceJSON.get("digital_temp") != null ? Double.parseDouble(deviceJSON.get("digital_temp").toString()) : null);
//            sensorReading.setGpioState(deviceJSON.get("gpio_state") != null ? Double.parseDouble(deviceJSON.get("gpio_state").toString()) : null);
//            sensorReading.setHumidity(deviceJSON.get("humidity") != null ? Double.parseDouble(deviceJSON.get("humidity").toString()) : null);
//            sensorReading.setId(deviceJSON.get("id") != null ? Integer.parseInt(deviceJSON.get("id").toString()) : null);
//            sensorReading.setLight(deviceJSON.get("light") != null ? Integer.parseInt(deviceJSON.get("light").toString()) : null);
//            sensorReading.setPressure(deviceJSON.get("pressure") != null ? Integer.parseInt(deviceJSON.get("pressure").toString()) : null);
//
//            sensorList.add(sensorReading);
//        }
//
//        return sensorList;
//    }
    private void createTextFile(ArrayList<Device> deviceList) throws ParseException, FileNotFoundException, IOException {
        int counter = 1;
        int xcounter = 0;
        int ycounter = 0;

        /*calculating number of rows in sensor reading json*/
        String deviceURI = "23100015";
        String jsonString = connectToSOLR(urlForSensorReadings + deviceURI + "?start_time=" + startTime + "&end_time=" + endTime);
        JSONParser jSONParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jSONParser.parse(jsonString);

        /*  */
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        StringBuffer outputText = new StringBuffer();
        int deviceCount = 1;
        String rowValue = "";
        for (Device device : deviceList) {

            String deviceName = (device.getLocation().getPrintName() != null ? device.getLocation().getPrintName() + "." : "") + (device.getPrintName() != null ? device.getPrintName() : "device" + counter++);
            ArrayList<Sensor> sensorList = device.getSensorList();
            for (Sensor sensor : sensorList) {
                String sensorName = deviceName + "." + sensor.getSensorType();
                rowValue = rowValue + "Health_" + sensorName;
                if (!(deviceCount == deviceList.size())) {
                    rowValue += ",";
                }
            }
            ycounter++;
            if (ycounter > ypos.length - 1) {
                ycounter = 0;
                xcounter++;
            }
            deviceCount++;
        }

        counter = 1;
        xcounter = 0;
        ycounter = 0;
        deviceCount = 1;
        for (Device device : deviceList) {

            String deviceName = (device.getLocation().getPrintName() != null ? device.getLocation().getPrintName() + "." : "") + (device.getPrintName() != null ? device.getPrintName() : "device" + counter++);
            ArrayList<Sensor> sensorList = device.getSensorList();
            for (Sensor sensor : sensorList) {
                String sensorName = deviceName + "." + sensor.getSensorType();
                rowValue = rowValue + sensorName;
                if (!(deviceCount == deviceList.size())) {
                    rowValue += ",";
                }
            }
            ycounter++;
            if (ycounter > ypos.length - 1) {
                ycounter = 0;
                xcounter++;
            }
            deviceCount++;
        }
        outputText.append(rowValue+"\n");





        HashMap<Integer, String> textFileRows = new HashMap<Integer, String>();
        for (int i = 0; i < jsonArray.size(); i++) {
            String initial = "";
            JSONObject jSONObject = (JSONObject) jsonArray.get(i);
            long time = (long) Double.parseDouble(jSONObject.get("timestamp").toString());
            initial += dateFormat.format(new Date(time)) + ",";
            for (int j = 0; j < totalSensorCount; j++) {
                initial += "Y" +  ",";
            }
            textFileRows.put(i, initial);
        }

        deviceCount = 1;

        for (Device device : deviceList) {

            
            ArrayList<Sensor> sensorList = device.getSensorList();

            deviceURI = device.getUri();
            deviceURI = "23100015";
            jsonString = connectToSOLR(urlForSensorReadings + deviceURI + "?start_time=" + startTime + "&end_time=" + endTime);
            jsonArray = (JSONArray) jSONParser.parse(jsonString);
            for (int i = 0; i < jsonArray.size(); i++) {
                
                String fileRow = textFileRows.get(i);

                JSONObject sensorReadingObj = (JSONObject) jsonArray.get(i);
                int sensorCount = 1;
                for (Sensor sensor : sensorList) {
                    fileRow = fileRow + sensorReadingObj.get(sensor.getSensorId()) + ((sensorCount==sensorList.size() && deviceCount == deviceList.size()) ? "" : ",");
                    sensorCount++;
                }

                textFileRows.put(i, fileRow);
            }

            ycounter++;
            if (ycounter > ypos.length - 1) {
                ycounter = 0;
                xcounter++;
            }
            deviceCount++;
        }
        FileWriter fstream = new FileWriter(textOuputURL);
        BufferedWriter out = new BufferedWriter(fstream);

        for (int i = 0; i < jsonArray.size(); i++) {
            outputText.append(textFileRows.get(i) + "\n");
        }
        out.write(outputText.toString());


    }
//
//    private void createTextFile2(ArrayList<Device> deviceList) throws ParseException, FileNotFoundException, IOException {
//        int counter = 1;
//        int xcounter = 0;
//        int ycounter = 0;
//        StringBuffer outputText = new StringBuffer();
//
//        int deviceCount = 1;
//        String rowValue = "";
//        for (Device device : deviceList) {
//
//            String deviceName = (device.getLocation().getPrintName() != null ? device.getLocation().getPrintName() + "." : "") + (device.getPrintName() != null ? device.getPrintName() : "device" + counter++);
//            ArrayList<Sensor> sensorList = device.getSensorList();
//            for (Sensor sensor : sensorList) {
//                String sensorName = deviceName + "." + sensor.getSensorType();
//                rowValue = rowValue + "Health_" + sensorName;
//                if (!(deviceCount == deviceList.size())) {
//                    rowValue += ",";
//                }
//            }
//            ycounter++;
//            if (ycounter > ypos.length - 1) {
//                ycounter = 0;
//                xcounter++;
//            }
//            deviceCount++;
//        }
//
//        for (Device device : deviceList) {
//
//            String deviceName = (device.getLocation().getPrintName() != null ? device.getLocation().getPrintName() + "." : "") + (device.getPrintName() != null ? device.getPrintName() : "device" + counter++);
//            ArrayList<Sensor> sensorList = device.getSensorList();
//            for (Sensor sensor : sensorList) {
//                String sensorName = deviceName + "." + sensor.getSensorType();
//                rowValue = rowValue + sensorName;
//                if (!(deviceCount == deviceList.size())) {
//                    rowValue += ",";
//                }
//            }
//            ycounter++;
//            if (ycounter > ypos.length - 1) {
//                ycounter = 0;
//                xcounter++;
//            }
//            deviceCount++;
//        }
//        outputText.append(rowValue);
//
//    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException, JAXBException, PropertiesFileNotFoundException, IOException {
        // TODO code application logic here
//        SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
//        long milli = 1359581003000l;
//        System.out.println(dateFormat.format(new Date(milli)));
        Generator generator = new Generator();
        String jsonString = connectToSOLR(inputJsonURL);
        ArrayList<Device> deviceList = generator.parseJSONAndBuildDeviceList(jsonString);
        generator.buildObjectForXMLGeneration(deviceList);
        generator.createTextFile(deviceList);
    }
}
