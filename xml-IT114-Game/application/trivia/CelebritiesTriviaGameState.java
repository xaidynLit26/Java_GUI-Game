package trivia;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
* /**
 * * Xamarys Liranzo
 * 04/23/25
 * xml@njit.edu 
 */
 */

 /*
  * This class controls and maintins the status of the additon of 
  * players, the removal of players, the amount of players,
  * the scores, etc.
  * @param playerID is the player number 
  */
public class CelebritiesTriviaGameState implements Serializable {
   public String message;  // Original message from a client.
   public int senderID;    // The ID of the client who sent that message.
   public HashMap<Integer, Integer> playerScores = new HashMap<>();

   //Code Phase 05 
   public static int QUESTION_TIMER_SECONDS = 15;
   private boolean questionTimer;
   public boolean isQuestionTimer() {
       return questionTimer;
   }
   public void setQuestionTimer(boolean questionTimer) {
       this.questionTimer = questionTimer;
   }
   //end of code 

   public CelebritiesTriviaGameState() {
       playerScores = new HashMap<>();
   }
   public void addPlayer(int playerID) {
       playerScores.put(playerID, 0);
   }
   public void removePlayer(int playerID) {
       playerScores.remove(playerID);
   }
   public int getPlayerCount() {

    return playerScores.size();
}
public void incrementScore(int playerID) {
    int score = playerScores.get(playerID);
    score++;
    playerScores.replace(playerID, score);
}
public boolean hasAnyPlayerScored() {
    for (Integer score : playerScores.values()) {
        if (score > 0) {
            return true;
        }
    }
    return false;
}
public int getWinner() {
    ArrayList<Integer> maxKeys = new ArrayList<>();
    int maxValue = Integer.MIN_VALUE;
    for (Map.Entry<Integer, Integer> entry : playerScores.entrySet()) {
        int value = entry.getValue();
        if (value > maxValue) {
            maxKeys.clear();
            maxKeys.add(entry.getKey());
            maxValue = value;
        } else if (value == maxValue) {
            maxKeys.add(entry.getKey());
        }
    }
    // there is more then 1 player with the highest score
    if (maxKeys.size() > 1)
        return -1;
    else
        return maxKeys.get(0);
}
public void clearScores() {
    for (Map.Entry<Integer, Integer> entry : playerScores.entrySet()) {
        entry.setValue(0);
    }
}
}