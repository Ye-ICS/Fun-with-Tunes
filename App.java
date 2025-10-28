class App {
    public static void main(String[] args) {
        System.out.println("Hello");

        playTwinkle(30);
    }

    static void playTwinkle(int beatsPerMinute) {
        int[] notes = {StdMidi.C4, StdMidi.C4, StdMidi.G4, StdMidi.G4, StdMidi.A4, StdMidi.A4, StdMidi.G4};

        StdMidi.setTempo(beatsPerMinute);
        StdMidi.playNote(notes[0], 1);
        StdMidi.playNote(notes[1], 1);
        StdMidi.playNote(notes[2], 1);
        StdMidi.playNote(notes[3], 1);
        StdMidi.playNote(notes[4], 1);
        StdMidi.playNote(notes[5], 1);
        StdMidi.playNote(notes[6], 1);
    }

    static void playMoonlightSonata (int beatsPerMinute) {
        StdMidi.setTempo(beatsPerMinute);
        StdMidi.noteOn(StdMidi.C4);
        StdMidi.noteOn(StdMidi.G4);
        StdMidi.pause(2);
        StdMidi.noteOff(StdMidi.G4);
        StdMidi.pause(1);
        StdMidi.allNotesOff();
    }
}