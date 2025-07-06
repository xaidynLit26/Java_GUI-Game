package trivia;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList; 
/**
 * * Xamarys Liranzo
 * 04/23/25
 * xml@njit.edu 
 */

 /*
  * This class is change of managing the storage of questions as well as their 
  * answers, This class adds these questions into and ArrayList and stores the 
  * answers in there as well. 
  * The get method returns the current question .
  * It also randomly generates questions only displaying
  * three questions per round from the 25 questions
  * The size mthod returns size or length of questions within the ArrayList
  */
  public class CelebritiesTriviaGameQuestionsList {
    private static ArrayList<CelebritiesTriviaGameQuestion> questions = new ArrayList<>();
    private LinkedList<CelebritiesTriviaGameQuestion> roundQuestions;
    private static int QUESTIONS_PER_ROUND = 3;
    private static final String QUESTIONS_FILENAME = "trivia/resources/CelebritiesTriviaGameQuestions.csv";


    public CelebritiesTriviaGameQuestionsList() {
        // Initialize the questions list only if it is empty
        if (questions.isEmpty()) {
             try (
               InputStream inputStream = getClass().getClassLoader().getResourceAsStream(QUESTIONS_FILENAME);
               BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
           ) {
               String line;
               while ((line = reader.readLine()) != null) {
                   String[] parts = line.split(",", 2);
                   if (parts.length == 2) {
                       questions.add(new CelebritiesTriviaGameQuestion(parts[0].trim(), parts[1].trim()));
                   }
               }
           } catch (Exception e) {
               e.printStackTrace(); // Log error if file not found or reading fails
           }

        }
        
        // Shuffle the questions list again for each new round
        Collections.shuffle(questions);
        // Create a LinkedList to hold random questions for one round
        roundQuestions = new LinkedList<>();        
        // Add random questions to the roundQuestions list
        for (int i = 0; i < QUESTIONS_PER_ROUND; i++) {
            roundQuestions.add(questions.get(i));
        }
    }

    public CelebritiesTriviaGameQuestion get(int currentQuestionIndex) {
        return roundQuestions.get(currentQuestionIndex);
    }

    public int size() {
        // Return the size of the roundQuestions LinkedList
        return roundQuestions.size();
    }
}






       