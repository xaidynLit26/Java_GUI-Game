package trivia;
/**
 * * Xamarys Liranzo
 * 04/23/25
 * xml@njit.edu 
 */
/*This class checks to see if the users input is correct and matches
*the correct answer connected to the question. 
*@param Question holds the question 
*@param answer holds the answer 
*@param user is the user's answer 
*@return a boolean true or false if users gets the question correct
*/
public record CelebritiesTriviaGameQuestion(String question, String answer) {
    //This function checks to see if the users input is correct
    public boolean isCorrectAnswer(String user) {
        return this.answer.equalsIgnoreCase(user);
    }
 }