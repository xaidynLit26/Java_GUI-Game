package trivia;
/**
 * * Xamarys Liranzo
 * 04/23/25
 * xml@njit.edu 
 */
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;  

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import netgame.common.Client;
import trivia.view.CelebritiesTriviaGameMainPanel;

/*
 * This class is what is responsible for the user to be able to play the game through the 
 * GUI. 
 */
public class CelebritiesTriviaGamePlayerWindow extends JFrame {
    private static final int PORT = 37999; // Port number for the server.

    private static volatile boolean connected = false; // Tracks connection status.

    // Represents the connection to the Hub; used to send messages;
    // also receives and processes messages from the Hub.
    private static CelebritiesTriviaGameClient celebritiesTriviaGameClient;

    // Represents the GUI panel containing all buttons/etc
    private CelebritiesTriviaGameMainPanel mainPanel;

    public static void main(String[] args) {
        String host = "";
        if (args.length == 0) {
            host = JOptionPane.showInputDialog(
                null,
                "Enter the host name of the\ncomputer hosting the movie trivia game:", 
                "Movie Trivia Game",
                JOptionPane.QUESTION_MESSAGE);
        } else {
            host = args[0];
        }

        if (host.isEmpty()) {
            System.out.println("Host name cannot be empty. Exiting.");
            return;
        }
        new CelebritiesTriviaGamePlayerWindow(host);
    }

    /**
     * Called when the user clicks the Quit button or closes
     * the window by clicking its close box.
     */
    public void doQuit() {
        if (connected) {
            mainPanel.setServerDisconnected();
            celebritiesTriviaGameClient.disconnect();
        }
        this.dispose();
        try {
            Thread.sleep(1000); // Time for DisconnectMessage to actually be sent.
        } catch (InterruptedException e) {
        }
        connected = false;
        System.out.println("Disconnected from the server. Goodbye!");
    }

    /**
     * A Client connects to the Hub and is used to send messages to
     * and receive messages from a Hub. Messages received from the
     * Hub will be of type ForwardedMessage and will contain the
     * ID number of the sender and the string that was sent by
     * that user.
     */
    private class CelebritiesTriviaGameClient extends Client {

        /**
         * Opens a connection the server on a specified computer.
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
            if (message instanceof CelebritiesTriviaGameState) {
                CelebritiesTriviaGameState state = (CelebritiesTriviaGameState) message;
                System.out.println("Timer On? " + state.isQuestionTimer());
                mainPanel.updateScoreBoard(state.playerScores);
                mainPanel.updateQuestionTimer(state.isQuestionTimer());
                if (state.senderID != 0) {
                    System.out.println("Player  " + state.senderID + ": " + state.message);
                }
            } else if (message instanceof String) {
                System.out.println(message.toString());
                mainPanel.setMessage(message.toString());
                if(message.toString().contains("Time's up") ||
                    message.toString().contains("All players have answered")) {
                    System.out.println("Timer Off");
                    mainPanel.updateQuestionTimer(false);
                }

            }
        }

        /**
         * Called when the connection to the client is shut down because of some
         * error message. (This will happen if the server program is terminated.)
         */
        @Override
        protected void connectionClosedByError(String message) {
            System.out.println("Connection closed due to error: " + message);
            mainPanel.setServerDisconnected();
            mainPanel.setDisable();
            connected = false;
            celebritiesTriviaGameClient = null;
        }

        /**
         * Posts a message to the transcript when someone joins the chat room.
         */
        @Override
        protected void playerConnected(int newPlayerID) {
            System.out.println("Player " + newPlayerID + " joined the game.");
        }

        /**
         * Posts a message to the transcript when someone leaves the chat room.
         */
        @Override
        protected void playerDisconnected(int departingPlayerID) {
            System.out.println("Player " + departingPlayerID + " left the game.");
        }

    }

    /**
     * Constructor creates the window and starts the process of connecting
     * to the hub; the actual connection is done in a separate thread.
     * 
     * @param host The IP address or host name of the computer where the server is
     *             running.
     */
    private CelebritiesTriviaGamePlayerWindow(final String host) {
        this.setTitle("Celebrities Trivia Game");

        mainPanel = new CelebritiesTriviaGameMainPanel(this);
        this.add(mainPanel);
        this.pack();

        this.setLocation(200, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() { // calls doQuit if user closes window
            public void windowClosing(WindowEvent e) {
                doQuit();
            }
        });
        new Thread() {
            // This is a thread that opens the connection to the server. Since
            // that operation can block, it's not done directly in the constructor.
            // Once the connection is established, the user interface elements are
            // enabled so the user can send messages. The Thread dies after
            // the connection is established or after an error occurs.
            public void run() {
                try {
                    System.out.println("Connecting to " + host + "...");
                    celebritiesTriviaGameClient = new CelebritiesTriviaGameClient(host);
                    connected = true;
                    mainPanel.setEnable();
                    mainPanel.setServerConnected(celebritiesTriviaGameClient.getID());

                } catch (IOException e) {
                    System.out.println("Failed to connect to the server: " + e.getMessage());
                    System.out.println("Error: " + e);
                }
            }
        }.start();
    }

    public void send(String message) {
        celebritiesTriviaGameClient.send(message);
    }
}

