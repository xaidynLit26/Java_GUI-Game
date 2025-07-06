package trivia.view;
/**
 ** Xaidyn Liranzo
 * 04/23/25
 * IT114 004
 * Phase 05
 * xml@njit.edu 
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/*
 * This class is in charge of creating the status pannel for the game 
 * which will be used to be implemented in the Main Game Pannel 
 */
public class CelebritiesTriviaGameStatusPanel extends JPanel {

  private static final Color DARK_RED = Color.decode("#631e17");
  private static final Color CREME = Color.decode(("#d0b192"));
  private static final Color RED = Color.decode(("#bf2121"));

  private JLabel serverStatus;
  
  //This is the constructor for the class 
  public CelebritiesTriviaGameStatusPanel() {
    super();
    initialize();
  }

  //This creates the display box for the status of the players within the game 
  private void initialize() {
    this.setBackground(DARK_RED);
    this.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.setLayout( new BorderLayout());

    serverStatus = new JLabel();
    serverStatus.setFont(new Font("Arial", Font.BOLD, 16));

    this.add(serverStatus, BorderLayout.WEST);
    this.updateServerDisconnected();

  }
  //This function will set the connection status to the player 
  //@param playerID = holds the id of the player 
  public void updateServerConnected(int playerID) {
    serverStatus.setText("Player " + playerID);
    serverStatus.setForeground(Color.WHITE);
  }

  //THis funciton will set the connection status of the game to disconnected 

  public void updateServerDisconnected() {
    serverStatus.setText("Disconnected");
    serverStatus.setForeground(CREME);
  }

    public static void main(String[] args) throws InterruptedException {
      JFrame frame = new JFrame("[xml] Celebrities Trivia Game - Status Panel");

      CelebritiesTriviaGameStatusPanel panel = new CelebritiesTriviaGameStatusPanel();
      frame.add(panel);
      frame.pack();
      frame.setSize(686, 55);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);

      Thread.sleep(5000);
      panel.updateServerConnected(10);
      Thread.sleep(5000);
      panel.updateServerDisconnected();
  }
}