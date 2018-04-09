
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

public class WeatherClock {

    private RCServo servo;

    private HashMap<Integer, Integer> codeMap = new HashMap<>();

    private static final int ERROR=0;

    int lastWeather=ERROR;

    private static final int HOT=40;
    private static final int STORM=55;
    private static final int RAIN=70;
    private static final int CLOUDY=87;
    private static final int PCLOUDY=107;
    private static final int SUNNY=120;
    private static final int FOG=137;
    private static final int WINDY=153;
    private static final int RAIN_AND_SNOW=167;
    private static final int SNOW=188;
    private static final int COLD=200;

    public WeatherClock(){
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

//            servo.openAny();
//            servo.waitForAttachment();
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
        TimerTask t = new TimerTask(){
            public void run() {
                try {
                    int weather = getWeather();

                    if (weather!=lastWeather && lastWeather!=ERROR){
                        //AudioPlayer.player.start(new AudioStream(getClass().getResourceAsStream("/thunder.wav")));
                    }

                    servo.setTargetPosition(weather);

                    if (weather!=ERROR){
                        lastWeather=weather;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };

        new Timer().schedule(t, 5000, 5*60*1000);
    }

    public int getWeather() {
        try {
            Document doc = new SAXBuilder().build(new URL("http://weather.yahooapis.com/forecastrss?p=10025"));

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
        new WeatherClock().start();
    }
}
