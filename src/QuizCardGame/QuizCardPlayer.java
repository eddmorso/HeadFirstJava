package QuizCardGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class QuizCardPlayer {

    private JTextArea display, answer;
    private ArrayList<QuizCard> cards;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JFrame jFrame;
    JButton nextButton;
    private boolean isShowAnswer;

    public static void main(String ... args){
        QuizCardPlayer quizCardPlayer = new QuizCardPlayer();
        quizCardPlayer.init();
    }

    public void init(){
        jFrame = new JFrame("Quiz Card Player");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        display = new JTextArea(10,20);
        display.setFont(bigFont);

        display.setLineWrap(true);
        display.setEditable(false);

        JScrollPane questionScrollPane = new JScrollPane(display);
        questionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        questionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        nextButton = new JButton("Show Question");
        mainPanel.add(questionScrollPane);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isShowAnswer){
                    display.setText(currentCard.getAnswer());
                    nextButton.setText("Next Card");
                    isShowAnswer = false;
                }else{
                    if(currentCardIndex < cards.size()){
                        showNextCard();
                    }else{
                        display.setText("That was the last card...");
                        nextButton.setEnabled(false);
                    }
                }
            }
        });

        JMenuBar jMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load Card Set");
        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.showOpenDialog(jFrame);
                loadFile(jFileChooser.getSelectedFile());
            }
        });
        fileMenu.add(loadMenuItem);
        jMenuBar.add(fileMenu);

        jFrame.setJMenuBar(jMenuBar);
        jFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        jFrame.setSize(640,500);
        jFrame.setVisible(true);
    }

    public void loadFile(File file){
        cards = new ArrayList<QuizCard>();
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                makeCard(line);
            }
            bufferedReader.close();

        }catch (IOException e){
            System.out.println("Couldn't read the card file");
            e.printStackTrace();
        }
        showNextCard();
    }

    private void makeCard(String lineToParse){
        String [] result = lineToParse.split("/");
        QuizCard quizCard = new QuizCard(result[0],result[1]);
        cards.add(quizCard);
        System.out.println("made a card");
    }

    private void showNextCard(){
        currentCard = cards.get(currentCardIndex);
        currentCardIndex++;
        display.setText(currentCard.getQuestion());
        nextButton.setText("Show Answer");
        isShowAnswer = true;
    }
}
