package BeatBox;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class BeatBox {

    private JPanel mainPanel;
    private ArrayList<JCheckBox> checkBoxList;
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;
    private JFrame jFrame;

    String [] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal", "Hand Clap",
            "Hi Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cow bell", "Vibra slap",
            "Low-mid Tom", "High Agogo", "Open Hi Conga"};
    int [] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    public static void main(String [] args){
        BeatBox beatBox = new BeatBox();
        beatBox.buildGUI();
    }

    public void buildGUI(){
        jFrame = new JFrame("Cyber BeatBox.BeatBox");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        checkBoxList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);


        JButton upTempo = new JButton("Up Tempo");
        upTempo.addActionListener(new MyUpTempoListener());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Down Tempo");
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);

        JButton save = new JButton("Save");
        save.addActionListener(new MySaveActionListener());
        buttonBox.add(save);

        JButton restore = new JButton("Restore");
        restore.addActionListener(new MyRestoreActionListener());
        buttonBox.add(restore);

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for(int i = 0; i < 16; i++){
            nameBox.add(new Label(instrumentNames[i]));
        }

        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);

        jFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(16,16);
        grid.setVgap(1);
        grid.setHgap(2);

        mainPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);

        for(int i = 0; i < 256; i++){
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(false);
            checkBoxList.add(checkBox);
            mainPanel.add(checkBox);
        }

        setUpMidi();

        jFrame.setBounds(50,50,50,50);
        jFrame.pack();
        jFrame.setVisible(true);

    }

    public void setUpMidi(){
        try{
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void buildTrackAndStart(){
        int [] trackList = null;

        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for(int i = 0; i < 16; i++){
            trackList = new int[16];

            int key = instruments[i];

            for(int j = 0; j < 16; j++){
                JCheckBox jCheckBox = checkBoxList.get(j + 16*i);

                if(jCheckBox.isSelected()){
                    trackList[j] = key;
                }else{
                    trackList[j] = 0;
                }
            }
            makeTracks(trackList);
            track.add(makeEvent(192,1,127,0,16));
        }

        track.add(makeEvent(192,9,1,0,15));
        try{
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class MyStartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            buildTrackAndStart();
        }
    }

    public class MyStopListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            sequencer.stop();
        }
    }

    public class MyUpTempoListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor*1.03));
        }
    }

    public class MyDownTempoListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor*.97));
        }
    }

    public class MySaveActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean checkBoxState [] = new boolean[256];

            for(int i = 0; i < checkBoxState.length; i++){
                JCheckBox checkBox = (JCheckBox) checkBoxList.get(i);
                if(checkBox.isSelected()){
                    checkBoxState[i] = true;
                }
            }
            try{
                FileOutputStream fileOutputStream = new FileOutputStream(new File("./src/BeatBox/Checkbox.ser"));
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(checkBoxState);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public class MyRestoreActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

            boolean checkBoxState [] = null;

            try{
                FileInputStream fileInputStream = new FileInputStream(new File("./src/BeatBox/Checkbox.ser"));
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                checkBoxState = (boolean[]) objectInputStream.readObject();
            }catch (Exception ex){
                ex.printStackTrace();
            }

            for(int i = 0; i < checkBoxState.length; i++){
                if(checkBoxState[i]){
                    checkBoxList.get(i).setSelected(true);
                }
            }
            sequencer.stop();
        }
    }


    public void makeTracks(int [] list){
        for(int i = 0; i < 16; i++){
            int key = list[i];

            if(key != 0){
                track.add(makeEvent(144,9,key,100,i));
                track.add(makeEvent(128,9,key,100,i));
            }
        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick){
        MidiEvent event = null;
        try{
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        }catch (Exception e){
            e.printStackTrace();
        }
        return event;
    }
}
