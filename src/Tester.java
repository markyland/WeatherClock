
import com.phidget22.PhidgetException;
import com.phidget22.RCServo;
import org.jdom.input.*;
import org.jdom.*;

import java.net.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mzeldis
 * Date: Jan 23, 2007
 * Time: 11:44:20 AM
 * To change this template use File | Settings | File Templates.
 */

public class Tester {

    private RCServo servo;

    private HashMap<Integer, Integer> codeMap = new HashMap<Integer, Integer>();

    private static final int ERROR=30;

    int lastWeather=ERROR;

    private static final int CLOUDY=87;
    private static final int PCLOUDY=107;
    private static final int RAIN=70;
    private static final int SNOW=188;
    private static final int RAIN_AND_SNOW=167;
    private static final int STORM=55;
    private static final int FOG=137;
    private static final int WINDY=153;
    private static final int COLD=200;
    private static final int HOT=40;
    private static final int SUNNY=121;

    public Tester(){
        codeMap.put(0, STORM);
        codeMap.put(1, STORM);
        codeMap.put(2, STORM);
        codeMap.put(3, STORM);
        codeMap.put(4, STORM);
        codeMap.put(5, RAIN_AND_SNOW);
        codeMap.put(6, RAIN_AND_SNOW);
        codeMap.put(7, RAIN_AND_SNOW);
        codeMap.put(8, RAIN);
        codeMap.put(9, RAIN);
        codeMap.put(10, RAIN);
        codeMap.put(11, RAIN);
        codeMap.put(12, RAIN);
        codeMap.put(13, SNOW);
        codeMap.put(14, SNOW);
        codeMap.put(15, SNOW);
        codeMap.put(16, SNOW);
        codeMap.put(17, RAIN);
        codeMap.put(18, RAIN);
        codeMap.put(19, FOG);
        codeMap.put(20, FOG);
        codeMap.put(21, FOG);
        codeMap.put(22, FOG);
        codeMap.put(23, WINDY);
        codeMap.put(24, WINDY);
        codeMap.put(25, COLD);
        codeMap.put(26, CLOUDY);
        codeMap.put(27, CLOUDY);
        codeMap.put(28, CLOUDY);
        codeMap.put(29, PCLOUDY);
        codeMap.put(30, PCLOUDY);
        codeMap.put(31, SUNNY);
        codeMap.put(32, SUNNY);
        codeMap.put(33, SUNNY);
        codeMap.put(34, SUNNY);
        codeMap.put(35, RAIN);
        codeMap.put(36, HOT);
        codeMap.put(37, STORM);
        codeMap.put(38, STORM);
        codeMap.put(39, STORM);
        codeMap.put(40, RAIN);
        codeMap.put(41, SNOW);
        codeMap.put(42, SNOW);
        codeMap.put(43, SNOW);
        codeMap.put(44, PCLOUDY);
        codeMap.put(45, STORM);
        codeMap.put(46, SNOW);
        codeMap.put(47, STORM);

        try {
            servo = new RCServo();
            servo.open(5000);

            servo.setTargetPosition(0);
            servo.setEngaged(true);
        } catch (PhidgetException e) {
            e.printStackTrace();

            System.exit(0);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run() {
                try {
                    servo.setTargetPosition(ERROR);
                    servo.close();
                } catch (PhidgetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }

    public void start(){
        int num=2000;

        try {
            for (int i=0; i<10000; i++){
                servo.setTargetPosition(ERROR);
                Thread.sleep(num);
                servo.setTargetPosition(HOT);
                Thread.sleep(num);
                servo.setTargetPosition(STORM);
                Thread.sleep(num);
                servo.setTargetPosition(RAIN);
                Thread.sleep(num);
                servo.setTargetPosition(CLOUDY);
                Thread.sleep(num);
                servo.setTargetPosition(PCLOUDY);
                Thread.sleep(num);
                 servo.setTargetPosition(SUNNY);
                Thread.sleep(num);
                servo.setTargetPosition(FOG);
                Thread.sleep(num);
                servo.setTargetPosition(WINDY);
                Thread.sleep(num);
                servo.setTargetPosition(RAIN_AND_SNOW);
                Thread.sleep(num);
                servo.setTargetPosition(SNOW);
                Thread.sleep(num);
                servo.setTargetPosition(COLD);
                Thread.sleep(num);


            }
        } catch (PhidgetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public int getWeather() {
        try {
            Document doc = new SAXBuilder().build(new URL("http://weather.yahooapis.com/forecastrss?p=11577"));

            String code=doc.getRootElement().getChild("channel").getChild("item").getChild("condition", Namespace.getNamespace("yweather", "http://xml.weather.yahoo.com/ns/rss/1.0")).getAttributeValue("code");

            Integer ret=codeMap.get(Integer.parseInt(code));

            if (ret==null){
                return ERROR;
            }

            return ret;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return ERROR;
    }

    public static void main(String args[]){
        new Tester().start();
    }
}
