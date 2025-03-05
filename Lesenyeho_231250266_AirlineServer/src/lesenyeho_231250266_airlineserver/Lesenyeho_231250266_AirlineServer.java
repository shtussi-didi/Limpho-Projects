package lesenyeho_231250266_airlineserver;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Lesenyeho_231250266_AirlineServer {

    private static final int PORT = 1742;
    private static final String[][] passengerOnSeat = new String[2][4];

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started, waiting for clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

                String clientType = dataIn.readUTF();
                System.out.println("Client type: " + clientType);

                if (clientType.equalsIgnoreCase("Reservations")) {
                    handleReservationsClient(dataIn);
                } else if (clientType.equalsIgnoreCase("CheckIn")) {
                    handleCheckInClient(dataOut);
                }

                dataIn.close();
                dataOut.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleReservationsClient(DataInputStream dataIn) throws IOException {
        String seatName = dataIn.readUTF();
        String passengerName = dataIn.readUTF();
        assignSeat(seatName, passengerName);
    }

    private static void handleCheckInClient(DataOutputStream dataOut) throws IOException {
        String seatInfo = getSeatInfo();
        dataOut.writeUTF(seatInfo);
    }

    private static void assignSeat(String seatName, String passengerName) {
        int row = seatName.charAt(1) - '1';
        int column = seatName.charAt(0) - 'A';
        passengerOnSeat[column][row] = passengerName;
        printSeatAssignments();
    }

    private static String getSeatInfo() {
        StringBuilder seatInfo = new StringBuilder();
        for (int i = 0; i < passengerOnSeat.length; i++) {
            for (int j = 0; j < passengerOnSeat[i].length; j++) {
                seatInfo.append((char) ('A' + i)).append(j + 1).append(": ");
                seatInfo.append(passengerOnSeat[i][j] == null ? "Empty" : passengerOnSeat[i][j]);
                seatInfo.append("\n");
            }
        }
        return seatInfo.toString();
    }

    private static void printSeatAssignments() {
        System.out.println("Current Seat Assignments:");
        System.out.println(getSeatInfo());
    }
}
