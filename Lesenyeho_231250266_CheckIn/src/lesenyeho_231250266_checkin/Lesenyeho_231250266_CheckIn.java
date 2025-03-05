package lesenyeho_231250266_checkin;




import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Lesenyeho_231250266_CheckIn extends Application {

    private TextArea seatInfoArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage Stage) {
        Stage.setTitle("CheckIn");

        seatInfoArea = new TextArea();
        seatInfoArea.setEditable(false);

        Scene scene = new Scene(seatInfoArea, 400, 200);
        Stage.setScene(scene);
        Stage.show();

        connectToServer();
    }

    private void connectToServer() {
        try (Socket socket = new Socket("localhost", 1742);
             DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
             DataInputStream dataIn = new DataInputStream(socket.getInputStream())) {

            // Identify client type
            dataOut.writeUTF("CheckIn");

            // Receive seat information from the server
            String seatInfo = dataIn.readUTF();
            seatInfoArea.setText(formatSeatInfo(seatInfo));

        } catch (IOException e) {
            e.printStackTrace();
            seatInfoArea.setText("Unable to connect to server.");
        }
    }

    // Method to format seat information with row numbers
    private String formatSeatInfo(String seatInfo) {
        String[] lines = seatInfo.split("\n");
        StringBuilder formattedInfo = new StringBuilder();

        // Create arrays for row information (assuming a fixed layout of 4 rows and 2 columns)
        String[] rowA = new String[4];
        String[] rowB = new String[4];

        // Populate row arrays
        for (String line : lines) {
            if (line.startsWith("A")) {
                int index = Integer.parseInt(line.substring(1, 2)) - 1;
                rowA[index] = line.split(": ")[1];
            } else if (line.startsWith("B")) {
                int index = Integer.parseInt(line.substring(1, 2)) - 1;
                rowB[index] = line.split(": ")[1];
            }
        }

        // Format output with row numbers
        for (int i = 0; i < 4; i++) {
            formattedInfo.append(i).append("\t")  // Row number
                    .append(rowA[i] == null ? "Empty" : rowA[i]).append("\t")
                    .append(rowB[i] == null ? "Empty" : rowB[i]).append("\n");
        }

        return formattedInfo.toString();
    }
}
