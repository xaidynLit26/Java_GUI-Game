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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

/*THis class creates the message pannel display for the usage 
 * within the main game panel. 
  */
public class CelebritiesTriviaGameMessagePanel extends JPanel {

    private static final Color GOLD = Color.decode("#f1dfa9");
    private static final Color DARK_RED = Color.decode("#ca1818");
    private static final String IMAGE_FILENAME = "/trivia/resources/images/celeb.jpeg";

    JTextArea textArea;
    JPanel textPanel;

    //The constructor for this class 
    public CelebritiesTriviaGameMessagePanel() {
        this.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon(getClass().getResource(IMAGE_FILENAME));
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(686, 386, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
        imageLabel.setLayout(new GridBagLayout());

        textArea = new JTextArea(6, 18);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(GOLD);
        textArea.setFont(new Font("Arial", Font.BOLD, 18));

        BevelBorder customBorder = (BevelBorder) BorderFactory.createBevelBorder(
                BevelBorder.RAISED,
                GOLD,
                DARK_RED);

        textPanel = new JPanel();
        textPanel.setBackground(Color.BLACK);
        textPanel.setBorder(customBorder);
        textPanel.add(textArea);

        imageLabel.add(textPanel, new GridBagConstraints());

        this.add(imageLabel, BorderLayout.CENTER);
        this.clearText();
    }

    //This function makes it so that the text within the message pannel will be able to be deleted or removed. 
    public void clearText() {
        textPanel.setVisible(false);
        textArea.setText("");
        revalidate();
        repaint();
    }

    //This function makes it so that text is able to be displayed on the message panel 
    //@param text = holds the text inputting into the call of this function to allow
    //for the ability to place text within the message panel.
    public void setText(String text) {
        if (textArea != null) {
            textPanel.setVisible(true);
            textArea.setText(text);
            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("[xml] Celebrities Trivia Game - Message Panel");

        CelebritiesTriviaGameMessagePanel panel = new CelebritiesTriviaGameMessagePanel();
        frame.add(panel);
        frame.pack();
        frame.setSize(686, 386);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Thread.sleep(5000);
        panel.setText("Who is the oldest Kardashian sister (only put her first name).");
        Thread.sleep(5000);
        panel.setText("Who currently has the most Grammys?");
        Thread.sleep(5000);
        panel.clearText();
        Thread.sleep(5000);
        panel.setText("Game Over");
    }
}