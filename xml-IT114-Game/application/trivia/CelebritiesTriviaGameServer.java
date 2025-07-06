package trivia;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import netgame.common.Hub;

//code for Phase 05
import java.util.Timer;
import java.util.TimerTask;
//end of code 


/**
/**
 * * Xamarys Liranzo
 * 04/23/25
 * xml@njit.edu 
 * 
 *
 *
 * This class is in charge of regulating the games start,
 * Playing connection displays, determining if the players all answered
 * going on to the next quesiton, and the end of the game.
 *
 */

public class CelebritiesTriviaGameServer extends Hub {

    private final static int PORT = 37999;

    private CelebritiesTriviaGameState state;

    private CelebritiesTriviaGameQuestionsList questions;
    private int currentQuestionIndex = -1;
    private Map<Integer, String> answersReceived; // Tracks answers from each player.

    //code for Phase 05
    private Timer questionTimer;

    public CelebritiesTriviaGameServer() throws IOException {
        super(PORT);
        setAutoreset(true);
        state = new CelebritiesTriviaGameState();
        initializeNewGame();
    }

    /**
     * Starts a new game for the players
     * no return and no parameters
     */

    private void initializeNewGame() {
        state.clearScores();
        state.setQuestionTimer(false);//Phase 05
        sendToAll(state); //phase 04
        questions = new CelebritiesTriviaGameQuestionsList();
        currentQuestionIndex = -1;
        answersReceived = new HashMap<>();
    }

    //This function makes sure that the game restarts a new game if a player joins
    //@param playerId handles the players number
    //@param message holds a string from the player
    //no return
    @Override
    protected void messageReceived(int playerID, Object message) {
        if (message instanceof String) {
            String command = ((String) message).trim();

            if (command.equalsIgnoreCase("restart")) {
                if (state.getPlayerCount() >= 2) {
                    sendToAll("[xml] A new game is starting!");
                    initializeNewGame();
                    startGame();
                } else {
                    sendToAll("[xml] Waiting for at least 2 players to start a new game.");
                }
            } else {
                handleAnswer(playerID, command);
            }
        }
    }
    /*
     * This function checks if there is enough players for the game
     * @param playerID holds the players information
     */

    @Override
    protected void playerConnected(int playerID) {
        System.out.println("[xml] Player connected: " + playerID);
        state.addPlayer(playerID);

        if (state.getPlayerCount() == 1) {
            sendToAll("[xml] Waiting for another player to join...");
        } else if (state.getPlayerCount() == 2) {
            sendToAll("[xml] Two players connected. Starting the game!");
            sendToAll(state);
            startGame();
        }
    }
    //This function checks to see if a player is disconnected and if so,
    //it will remove the disconnected player from the game and will wait for a
    //new player to connect again
    //@param playerID holds the players number value

    @Override
    protected void playerDisconnected(int playerID) {
        System.out.println("[xml] Player disconnected: " + playerID);
        state.removePlayer(playerID);
        sendToAll(state);

        if (state.getPlayerCount() < 2) {
            sendToAll("[xml] Player" + playerID +" disconnected. Waiting for another player to continue the game.");
            cancelQuestionTimer();//Phase 05 code
            initializeNewGame();
        }
        // Remove disconnected player's answers.
        synchronized (answersReceived) {
            answersReceived.remove(playerID);
        }
    }

    /**
     * Starts the trivia game.
     */
    private void startGame() {
        currentQuestionIndex = -1;
        nextQuestion();
    }

   /**
     * Handle answers from players.
     * @param playerID hold the value of the player
     * @param answer holds the users answer
     */

    private void handleAnswer(int playerID, String answer) {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {

            synchronized (answersReceived) {
                if (!answersReceived.containsKey(playerID)) {
                    answersReceived.put(playerID, answer);
                    System.out.println("[xml] Player " + playerID + " answered: " + answer);
                    //sendToAll("[xml] Player " + playerID + " answered: " + answer);
                    sendToOne(playerID, "Player " + playerID +
                     " has answered. Waiting for all players to answer...");

                    if (answersReceived.size() == state.getPlayerCount()) {
                        System.out.println("[xml] All players have answered.");
                        sendToAll("[xml] All players have answered.");
                        // All players have answered, cancel the timer and proceed.
                        cancelQuestionTimer();//Phase 05 code
                        evaluateAnswers();
                    }
                    sendToAll(state);
                }
            }
        }
    }

   /**
     * Moves to the next question and sets a timer for players to answer.
     */

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex >= questions.size()) {
            endGame();
            return;
        }

        CelebritiesTriviaGameQuestion currentQuestion = questions.get(currentQuestionIndex);
        sendToAll("[xml] Question: " + currentQuestion.question());
        startQuestionTimer();//Phase 05 code
        sendToAll(state);//Phase 05 code

    }

    /**
     * Evaluate the answers received for the current question.
     */
    private void evaluateAnswers() {
        CelebritiesTriviaGameQuestion currentQuestion = questions.get(currentQuestionIndex);

        for (Map.Entry<Integer, String> entry : answersReceived.entrySet()) {
            int playerID = entry.getKey();
            String answer = entry.getValue();

            if (currentQuestion.isCorrectAnswer(answer)) {
                state.incrementScore(playerID);
                sendToAll("[xml] Player " + playerID + " answered correctly! The answer was: " + currentQuestion.answer());
            } else {
                sendToAll("[xml] Player " + playerID + " answered incorrectly.");
            }
        }
        answersReceived = new HashMap<>();
        nextQuestion();
    }

    /**
     * End the game and declare the result.
     */
    private void endGame() {
        if (!state.hasAnyPlayerScored()) {
            sendToAll("[xml] The game ended with no correct answers. Better luck next time!");
        } else {
            int winner = state.getWinner();
            if (winner == -1) {
                sendToAll("[xml] The game ended in a tie!");
            } else {
                sendToAll("[xml] Player " + winner + " wins the game!");
            }
        }
        //sendToAll("[xml] Type 'restart' to play again.");
    }

    //Code for Phase 05
    //Initialize and handles the start timer for the questions
    private void startQuestionTimer() {
        state.setQuestionTimer(false);
        cancelQuestionTimer(); // Cancel any previous timer.
        state.setQuestionTimer(true);
        questionTimer = new Timer();
        questionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
              sendToAll("Time's up!");
              evaluateAnswers();
            }
        }, CelebritiesTriviaGameState.QUESTION_TIMER_SECONDS * 1000);
     }
     private void cancelQuestionTimer() {
        state.setQuestionTimer(false);
        if (questionTimer != null) {
            questionTimer.cancel();
            questionTimer = null;
        }
     }//end of code
     

    public static void main(String[] args) {
        try {
            new CelebritiesTriviaGameServer();
        } catch (IOException e) {
            System.out.println("[xml] Error starting server: " + e.getMessage());
        }
    }
}