import org.jdom.input.*;
import org.jdom.*;

import java.net.*;
import java.util.*;
import java.io.*;

import sun.audio.*;

/**
 * Created by IntelliJ IDEA.
 * User: mzeldis
 * Date: Jan 23, 2007
 * Time: 11:44:20 AM
 * To change this template use File | Settings | File Templates.
 */

public class Sounder {

    private int last_=-2;

    public void start(){
        TimerTask t = new TimerTask(){
            public void run() {
                int cur=getWeather();

                if (last_!=cur){
                    try {
                        AudioPlayer.player.start(new AudioStream(new FileInputStream("C:\\Documents and Settings\\Mark Zeldis\\Desktop\\thunder.wav")));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                last_=cur;
            }
        };

        new Timer().schedule(t, 0, 60*1000);
    }

    public int getWeather() {
        try {
            Document doc = new SAXBuilder().build(new URL("http://weather.yahooapis.com/forecastrss?p=10025"));

            String code=doc.getRootElement().getChild("channel").getChild("item").getChild("condition", Namespace.getNamespace ("yweather", "http://xml.weather.yahoo.com/ns/rss/1.0")).getAttributeValue("code");

            return Integer.parseInt(code);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static void main(String args[]){
        new Sounder().start();
    }
}