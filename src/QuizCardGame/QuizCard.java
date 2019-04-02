package QuizCardGame;

public class QuizCard {

    private String question, answer;

    public QuizCard(String question, String answer){
        this.answer = answer;
        this.question = question;
    }


    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
