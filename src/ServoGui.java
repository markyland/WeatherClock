import com.phidget22.PhidgetException;
import com.phidget22.RCServo;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Mark Zeldis
 * Date: Jan 26, 2007
 * Time: 8:05:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServoGui {

    private RCServo servo;

    public ServoGui(){
        init();
    }

    private void init(){
        try {
            servo = new RCServo();

       //     servo.setDeviceSerialNumber(19774);
//            servo.setIsHubPortDevice(true);
//            servo.setHubPort(6);
            servo.open(5000);

            servo.setTargetPosition(0);
            servo.setEngaged(true);
        }
        catch (PhidgetException e) {
            e.printStackTrace();

            System.exit(0);
        }
        JFrame f = new JFrame("Servo Gui");

        JPanel p = new JPanel();

        final JTextField degreesTF = new JTextField(4);

        JButton button = new JButton("Turn");
        button.addActionListener(new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                try {
                    int degrees = Integer.parseInt(degreesTF.getText().trim());

                    servo.setTargetPosition(degrees);
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        p.add(new JLabel("Degrees : "));
        p.add(degreesTF);
        p.add(button);

        f.getContentPane().add(p);

        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String args[]){
          new ServoGui();
    }
}
