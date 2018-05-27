
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import javax.swing.*;

import arduino.*;



public class ArduinoWindow
{
    static Arduino arduino;
    static JFrame frame = new JFrame("An LED Controller");
    static JButton btnOn = new JButton("ON");
    static JButton btnOff = new JButton("OFF");
    static JButton submitButton = new JButton("Submit");
    static JTextField textField = new JTextField();
    static JTextField receiverField = new JTextField();
    static JButton btnRefresh;

    public static void main(String[] args) {
        ArduinoWindow aw = new ArduinoWindow();

        setUpGUI();
        ActionListener task = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String message;
                message = arduino.serialRead();
                receiverField.setText(message);            }
        };
        Timer timer = new Timer(100 ,task); // Execute task each 100 miliseconds
        timer.setRepeats(true);
        timer.start();

        frame.setResizable(false);

        btnOn.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent e) {
                arduino.serialWrite('1');

            }
        });

        btnOff.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent e) {
                arduino.serialWrite('0');
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                arduino.setBaudRate(4800);
                String message = textField.getText();
                int stringLength = message.length();
                arduino.serialWrite(message,stringLength, 0);
                textField.setText("");

            }
        });



    }

    public static void populateMenu(){ //gets the list of available ports and fills the dropdown menu
        final PortDropdownMenu portList = new PortDropdownMenu();
        portList.refreshMenu();
        final JButton connectButton = new JButton("Connect");


        ImageIcon refresh = new ImageIcon("D:\\LiFi Project 10-May\refresh.png");
        btnRefresh = new JButton(refresh);

        JPanel topPanel = new JPanel();

        btnRefresh.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                portList.refreshMenu();

            }
        });
        topPanel.add(portList);
        topPanel.add(btnRefresh);
        topPanel.add(connectButton);
        // populate the drop-down box

        connectButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(connectButton.getText().equals("Connect")){
                    arduino = new Arduino(portList.getSelectedItem().toString(),4800);
                    if(arduino.openConnection()){
                        connectButton.setText("Disconnect");
                        portList.setEnabled(false);
                        btnOn.setEnabled(true);
                        btnOff.setEnabled(true);
                        btnRefresh.setEnabled(false);
                        frame.pack();
                    }
                }
                else {
                    arduino.closeConnection();
                    connectButton.setText("Connect");;
                    portList.setEnabled(true);
                    btnOn.setEnabled(false);
                    btnRefresh.setEnabled(true);
                    btnOff.setEnabled(false);
                }
            }

        });
        //topPanel.setBackground(Color.BLUE);
        frame.add(topPanel, BorderLayout.NORTH);
    }

    public static void setUpGUI(){
        frame.setSize(600, 600);
        frame.setResizable(true);
        frame.setBounds(400, 100, 500, 500);
        frame.setBackground(Color.black);
        frame.setForeground(Color.black);
        frame.setPreferredSize(new Dimension(600,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        btnOn.setForeground(Color.GREEN);
        btnOn.setEnabled(false);
        btnOff.setForeground(Color.RED);
        btnOff.setEnabled(false);
        textField.setPreferredSize(new Dimension (200, 50));
        receiverField.setPreferredSize(new Dimension(400, 100));
        //textField.setLocation(800, 800);
        JPanel pane = new JPanel();
        //pane.setBackground(Color.blue);
        pane.add(btnOn);
        pane.add(textField);
        pane.add(btnOff);
        pane.add(submitButton);
        pane.add(receiverField);
        frame.add(pane, BorderLayout.CENTER);
        populateMenu();
        frame.pack();
        frame.getContentPane();
        frame.setVisible(true);

    }


//

}
