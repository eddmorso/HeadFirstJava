package QuizCardGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class QuizCardBuilder {
    private JTextArea question, answer;
    private ArrayList<QuizCard> cards;
    private JFrame jFrame;

    public static void main(String...args){
        QuizCardBuilder builder = new QuizCardBuilder();
        builder.init();
    }

    public void init(){

        jFrame = new JFrame("Quiz Card Builder");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);
        question = new JTextArea(6,20);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(bigFont);

        JScrollPane questionScroller = new JScrollPane(question);
        questionScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        questionScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        answer = new JTextArea(6,20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(bigFont);

        JScrollPane answerScroller = new JScrollPane(answer);
        answerScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        answerScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton nextButton = new JButton("Next card");

        cards = new ArrayList<QuizCard>();

        JLabel questionLabel = new JLabel("Question:");
        JLabel answerLabel = new JLabel("Answer:");

        mainPanel.add(questionLabel);
        mainPanel.add(questionScroller);
        mainPanel.add(answerLabel);
        mainPanel.add(answerScroller);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuizCard quizCard = new QuizCard(question.getText(), answer.getText());
                cards.add(quizCard);
                clearCard();
            }
        });
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.clear();
                clearCard();
            }
        });
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuizCard quizCard = new QuizCard(question.getText(), answer.getText());
                cards.add(quizCard);

                JFileChooser fileSave = new JFileChooser();
                fileSave.showSaveDialog(jFrame);
                saveFile(fileSave.getSelectedFile());
            }
        });
        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        jFrame.setJMenuBar(menuBar);
        jFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        jFrame.setSize(500,600);
        jFrame.setVisible(true);

    }

    private void clearCard(){
        question.setText("");
        answer.setText("");
        question.requestFocus();
    }

    private void saveFile(File file){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(QuizCard quizCard : cards){
                writer.write(quizCard.getQuestion() + "/");
                writer.write(quizCard.getAnswer() + "\n");
            }
            writer.close();
        }catch(IOException e){
            System.out.println("Couldn't write the cardList out");
            e.printStackTrace();
        }
    }
}
