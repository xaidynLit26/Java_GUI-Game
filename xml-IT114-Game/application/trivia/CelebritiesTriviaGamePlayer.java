/**
 * * Xamarys Liranzo
 * 04/23/25
 * xml@njit.edu 
 */

package trivia;
import java.io.IOException;
import java.util.Scanner;
import netgame.common.Client;

/**
 *This class is what handles the actions of the player within the trivia
 * game. It allows for player to connect to a server and allows them to
 * play.
 *
 */

public class CelebritiesTriviaGamePlayer {

    private static final int PORT = 37829; // Port number for the server.

    private static volatile boolean connected = false; // Tracks connection status.
    private static CelebritiesTriviaGameClient celebritiesTriviaGameClient;
    

    public static void main(String[] args) {
        String host = "";
        Scanner scanner = new Scanner(System.in);
        if (args.length == 0) {
            System.out.print("[xml] Enter the host name of the computer hosting the trivia game: ");
            host = scanner.nextLine().trim();
        } else {
            host = args[0];
        }

        if (host.isEmpty()) {
            System.out.println("[xml] Host name cannot be empty. Exiting.");
            scanner.close();
            return;
        }

        // Used to establish a connection to the server.
        try {
            System.out.println("[xml] Connecting to " + host + "...");
            celebritiesTriviaGameClient = new CelebritiesTriviaGameClient(host);
            connected = true;
            System.out.println("[xml] Connected to the server. Type your messages below. Type 'quit' to exit.");
        } catch (IOException e) {
            System.out.println("[xml] Failed to connect to the server: " + e.getMessage());
            scanner.close();
            return;
        }

        // This look is in change of sending messages (IMPORTANT)
        while (connected) {
            String message = scanner.nextLine().trim();

            if (message.equalsIgnoreCase("quit")) {
                doQuit();
                break;
            }

            if (!message.isEmpty()) {
                celebritiesTriviaGameClient.send(message);
            }
        }

        scanner.close();
    }

    /**
     * This is the method that handles the players disconnecting from the
     * game
     */

    private static void doQuit() {
        if (connected) {
            celebritiesTriviaGameClient.disconnect();
            try {
                Thread.sleep(1000); // Time for DisconnectMessage to actually be sent.
            } catch (InterruptedException e) {
            }
            connected = false;
            System.out.println("[xml] Disconnected from the server. Goodbye!");
        }
    }

    /**
     * Inner class representing the trivia game client.
     */
    private static class CelebritiesTriviaGameClient extends Client {

        /**
         * Constructor to create a client connection to the specified host.
         *
         * @param host The server's host name or IP address.
         * @throws IOException If the connection cannot be established.
         */
        CelebritiesTriviaGameClient(String host) throws IOException {
            super(host, PORT);
        }

        /**
         * Called when a message is received from the server.
         *
         * @param message The received message.
         */
        @Override
        protected void messageReceived(Object message) {
            //For Next Phase Change 
            System.out.println(message.toString());
            if (message instanceof CelebritiesTriviaGameState) {
                CelebritiesTriviaGameState state = (CelebritiesTriviaGameState) message;
                if (state.senderID != 0) {
                    System.out.println("[xml] Player  " + state.senderID + " " + state.message);
                }
            }
        }

        /**
         * Called when the connection is closed due to an error.
         *
         * @param message Error message describing the reason for disconnection.
         */
        @Override
        protected void connectionClosedByError(String message) {
            System.out.println("[xml] Connection closed due to error: " + message);
            connected = false;
        }

        /**
         * Called when a new player connects to the server.
         *
         * @param newPlayerID The ID of the newly connected player.
         */
        @Override
        protected void playerConnected(int newPlayerID) {
            System.out.println("[xml] Player " + newPlayerID + " joined the game.");
        }

        /**
         * Called when a player disconnects from the server.
         *
         * @param departingPlayerID The ID of the player who disconnected.
         */
        @Override
        protected void playerDisconnected(int departingPlayerID) {
            System.out.println("[xml] Player " + departingPlayerID + " left the game.");
        }
    }
}