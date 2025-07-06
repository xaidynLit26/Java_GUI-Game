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
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import trivia.CelebritiesTriviaGamePlayerWindow;

/*
 * This class is what is resposible for incooperating all the aspects of the game screen 
 * together for the user to be able to plau with. It will be the screen that will pop up 
 * when the user decides to begin the game 
 */

public class CelebritiesTriviaGameMainPanel extends JPanel {

  //Colors used for the main panel and its other properties
  private static final Color BLUE = Color.decode("#3b639e");
  private static final Color RED = Color.decode(("#bf2121"));
  private static final Color TAN = Color.decode("#d0b192");
  private static final Color DARK_RED = Color.decode("#ca1818");


  private CelebritiesTriviaGameStatusPanel statusPanel;
  private CelebritiesTriviaGameMessagePanel messagePanel;
  private CelebritiesTriviaGameScoreBoardPanel scoreBoardPanel;

  private CelebritiesTriviaGamePlayerWindow window;

  private JLabel answerLabel;
  private JTextField answerText;
  private JButton sendButton;
  private JButton restartButton;
  private JButton quitButton;

  //Contsructor for the class 
  public CelebritiesTriviaGameMainPanel(CelebritiesTriviaGamePlayerWindow window) {
    super();
    this.window = window;
    initialize();
  }

  //This method creates the screen or Main Panel in which it makes 
  //new objects of each portion of the players screen and places them 
  //within the main game panel
  private void initialize() {
    this.setBackground(Color.BLACK);
    this.setLayout(new BorderLayout(2, 2));

    statusPanel = new CelebritiesTriviaGameStatusPanel();
    this.add(statusPanel, BorderLayout.NORTH);

    scoreBoardPanel = new CelebritiesTriviaGameScoreBoardPanel();
    this.add(scoreBoardPanel, BorderLayout.EAST);

    messagePanel = new CelebritiesTriviaGameMessagePanel();
    this.add(messagePanel, BorderLayout.WEST);

    JPanel controlPanel = new JPanel();
    controlPanel.setBackground(RED);

    answerLabel = new JLabel("Answer:");
    answerLabel.setForeground(TAN);
    answerLabel.setFont(new Font("Arial", Font.BOLD, 16));
    controlPanel.add(answerLabel);

    answerText = new JTextField(40);
    controlPanel.add(answerText);

    sendButton = new JButton("Send");
    sendButton.setForeground(BLUE);
    sendButton.setFont(new Font("Arial", Font.BOLD, 16));
    controlPanel.add(sendButton);

    controlPanel.add(Box.createHorizontalStrut(30));
    restartButton = new JButton("Restart");
    restartButton.setForeground(BLUE);
    restartButton.setFont(new Font("Arial", Font.BOLD, 16));
    controlPanel.add(restartButton);
    quitButton = new JButton("Quit");
    quitButton.setForeground(BLUE);
    quitButton.setFont(new Font("Arial", Font.BOLD, 16));
    controlPanel.add(quitButton);

    answerText.addActionListener(this::sendAnswer);
    sendButton.addActionListener(this::sendAnswer);
    restartButton.addActionListener(this::restartGame);
    quitButton.addActionListener(this::quitGame);


    //controlPanel.add(quitButton);
    this.add(controlPanel, BorderLayout.SOUTH);

    this.setDisable();
  }
  //This method allows for "disabling" of features for users in the game
  public void setDisable() {
    answerText.setEditable(false);
    answerText.setEnabled(false);
    answerText.setBackground(Color.LIGHT_GRAY);
    answerText.setText("");
    sendButton.setEnabled(false);
    restartButton.setEnabled(false);
  }

  //This method allows for the enabling of features for users in the game
  public void setEnable() {
    answerText.setEditable(true);
    answerText.setEnabled(true);
    answerText.setBackground(Color.WHITE);
    answerText.requestFocus();
    sendButton.setEnabled(true);
    restartButton.setEnabled(true);
  }

  //This makes the status of connected to be displayed on the users game 
  //@param playerID = holds the number of the player 
  public void setServerConnected(int playerID) {
    statusPanel.updateServerConnected(playerID);
  }

  //This makes the status of disconnected to be displayed on the users game 
  // it also clears the message panel and resets the properties within the score board panel 
  public void setServerDisconnected() {
    statusPanel.updateServerDisconnected();
    messagePanel.clearText();
    scoreBoardPanel.resetPlayers(new HashMap<>());
  }

  //This makes a message display on the message panel. 
  //@param message = takes in a message to be displayed
  public void setMessage(String message) {
    messagePanel.setText(message);
  }

  public void clearText() {
    messagePanel.setVisible(false);
    messagePanel.setText("");
    revalidate();
    repaint();
}

  //This displays the reset of players within the GUI 
  //@param playerScores = takes in the HashMap of a players score and resets it 
  public void updateScoreBoard(HashMap<Integer, Integer> playerScores) {
    scoreBoardPanel.resetPlayers(playerScores);
  }

  private void sendAnswer(ActionEvent event) {
    String message = answerText.getText();
    if (message.trim().length() == 0)
      return;
    window.send(message);
    answerText.setText("");
    answerText.requestFocus();
  }
 
 
  //This action controls the restart event of the game
  private void restartGame(ActionEvent event) {
    window.send("restart");
  }
 
  //This action controls the end of the game event
  private void quitGame(ActionEvent event) {
    window.doQuit();
  }

  //This event unpdates the timer based on boolean values
  public void updateQuestionTimer(boolean questionTimer) {
    if(questionTimer)
      scoreBoardPanel.startQuestionTimer();
    else
      scoreBoardPanel.stopTimer();
  }
 
 

  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame("[xml] Celebrities Trivia Game - Main Panel");

    CelebritiesTriviaGameMainPanel panel = new CelebritiesTriviaGameMainPanel(null);
    frame.add(panel);
    frame.pack();

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

  


    
  }
}