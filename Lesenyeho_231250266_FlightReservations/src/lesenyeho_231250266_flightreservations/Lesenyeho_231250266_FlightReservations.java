package lesenyeho_231250266_flightreservations;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Lesenyeho_231250266_FlightReservations {

    private Frame airplane;
    private Frame passenger;
    private TextField passengerNameField;
    private String selectedSeat;

    public static void main(String[] args) {
        new Lesenyeho_231250266_FlightReservations();
    }

    public Lesenyeho_231250266_FlightReservations() {
        setupAirplaneGUI();
        setupPassengerGUI();
    }

    private void setupAirplaneGUI() {
        airplane = new Frame("Reservations Airplane");
        airplane.setSize(300, 400);
        airplane.setLayout(null);

        
        String[] seats = {"A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4"};
        int x = 50, y = 50;

        for (int i = 0; i < seats.length; i++) {
            Button seatButton = new Button(seats[i]);
            seatButton.setBounds(x, y, 100, 40);
            airplane.add(seatButton);

            
            seatButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedSeat = seatButton.getLabel();
                    seatButton.setVisible(false); 
                    airplane.setVisible(false);   
                    passenger.setVisible(true);   
                }
            });

            y += 50;
            if (i == 3) {  
                x += 120;
                y = 50;
            }
        }

        airplane.setVisible(true);
    }

    private void setupPassengerGUI() {
        passenger = new Frame("Reservations Passenger");
        passenger.setSize(300, 100);
        passenger.setLayout(null);

        Label seatLabel = new Label("Seat");
        seatLabel.setBounds(20, 40, 50, 20);
        passenger.add(seatLabel);

        passengerNameField = new TextField();
        passengerNameField.setBounds(80, 40, 200, 20);
        passenger.add(passengerNameField);

        passengerNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String passengerName = passengerNameField.getText();
                    Communicator communicator = new Communicator();
                    communicator.sendToServer("Reservations", selectedSeat, passengerName);
                    passengerNameField.setText(""); 
                    passenger.setVisible(false);    
                    airplane.setVisible(true);      
                }
            }
        });

        passenger.setVisible(false);
    }
}

class Communicator {
    public void sendToServer(String clientType, String seat, String passengerName) {
        try (Socket socket = new Socket("localhost", 1742);
             DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())) {
            
            dataOut.writeUTF(clientType);
            dataOut.writeUTF(seat);
            dataOut.writeUTF(passengerName);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
