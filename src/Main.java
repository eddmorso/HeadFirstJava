import javax.sound.midi.*;
public class Main {

    public static void main(String[] args) {
        Main main = new Main();

        //main.play(102, 30);
        //main.play(80, 20);
        main.play(40, 70);
    }

    public void play(int instrument, int note){
        try {
            Sequencer player = MidiSystem.getSequencer();
            player.open();

            Sequence sequence = new Sequence(Sequence.PPQ, 4);

            Track track = sequence.createTrack();

            MidiEvent midiEvent = null;

            ShortMessage first = new ShortMessage();
            first.setMessage(192,1,instrument,0);
            MidiEvent changeInstrument = new MidiEvent(first, 1);
            track.add(changeInstrument);

            ShortMessage shortMessage = new ShortMessage();
            shortMessage.setMessage(144,1,note,100);
            MidiEvent noteOn = new MidiEvent(shortMessage, 1);
            track.add(noteOn);

            ShortMessage shortMessage1 = new ShortMessage();
            shortMessage1.setMessage(128,1,note,100);
            MidiEvent noteOff = new MidiEvent(shortMessage1, 20);
            track.add(noteOff);

            player.setSequence(sequence);

            player.start();
        }catch (Exception e){
            System.out.println("Bummer");
            e.printStackTrace();
        }
    }
}
