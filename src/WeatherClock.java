
import com.phidget22.PhidgetException;
import com.phidget22.RCServo;
import org.jdom.input.*;
import org.jdom.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
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

    private static final int HOT=10;
    private static final int STORM=28;
    private static final int RAIN=43;
    private static final int CLOUDY=59;
    private static final int PCLOUDY=78;
    private static final int SUNNY=93;
    private static final int FOG=108;
    private static final int WINDY=124;
    private static final int RAIN_AND_SNOW=138;
    private static final int SNOW=158;
    private static final int COLD=174;

    public WeatherClock(){
        codeMap.put(1, SUNNY);
        codeMap.put(2, SUNNY);
        codeMap.put(3, PCLOUDY);
        codeMap.put(4, PCLOUDY);
        codeMap.put(5, FOG);
        codeMap.put(6, CLOUDY);
        codeMap.put(7, CLOUDY);
        codeMap.put(8, CLOUDY);
        codeMap.put(11, FOG);
        codeMap.put(12, RAIN);
        codeMap.put(13, RAIN);
        codeMap.put(14, RAIN);
        codeMap.put(15, STORM);
        codeMap.put(16, STORM);
        codeMap.put(17, STORM);
        codeMap.put(18, RAIN);
        codeMap.put(19, SNOW);
        codeMap.put(20, SNOW);
        codeMap.put(21, SNOW);
        codeMap.put(22, SNOW);
        codeMap.put(23, SNOW);
        codeMap.put(24, RAIN_AND_SNOW);
        codeMap.put(25, RAIN_AND_SNOW);
        codeMap.put(26, RAIN_AND_SNOW);
        codeMap.put(29, RAIN_AND_SNOW);
        codeMap.put(30, HOT);
        codeMap.put(31, COLD);
        codeMap.put(32, WINDY);
        codeMap.put(33, SUNNY);
        codeMap.put(34, SUNNY);
        codeMap.put(35, PCLOUDY);
        codeMap.put(36, PCLOUDY);
        codeMap.put(37, FOG);
        codeMap.put(38, CLOUDY);
        codeMap.put(39, RAIN);
        codeMap.put(40, RAIN);
        codeMap.put(41, STORM);
        codeMap.put(42, STORM);
        codeMap.put(43, SNOW);
        codeMap.put(44, SNOW);

        try {
            servo = new RCServo();

            servo.open(5000);

            setTargetPosition(0);
        }
        catch (PhidgetException e) {
            e.printStackTrace();

            System.exit(0);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run() {
                try {
                    setTargetPosition(ERROR);
                    servo.close();
                } catch (PhidgetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }

    public void setTargetPosition(double pos) {
        try {
            servo.setTargetPosition(pos);
            servo.setEngaged(true);
            Thread.sleep(4000);
            servo.setEngaged(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        TimerTask t = new TimerTask(){
            public void run() {
                try {
                    int weather = getWeather();

                    if (weather!=lastWeather && lastWeather!=ERROR){
                        //AudioPlayer.player.start(new AudioStream(getClass().getResourceAsStream("/thunder.wav")));
                    }

                    setTargetPosition(weather);

                    if (weather!=ERROR){
                        lastWeather=weather;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };

        new Timer().schedule(t, 0, 1800000);
    }

    public int getWeather() {
        try {
            String s = readStringFromURL("http://dataservice.accuweather.com/currentconditions/v1/2650_PC?apikey=wKhQuqGjs5mQatvpLwmR8UNvFXsLsknP");

            String searchText="\"WeatherIcon\":";

            int startIndex=s.indexOf(searchText)+searchText.length();

            String code=s.substring(startIndex, s.indexOf(",", +startIndex));

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

    private static String readStringFromURL(String requestURL) throws IOException {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(), StandardCharsets.UTF_8.toString())){
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    public static void main(String args[]){
        new WeatherClock().start();
    }
}
