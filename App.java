class App {
    public static void main(String[] args) {
        System.out.println("Hello");

        playTwinkle(30);
    }

    static void playTwinkle(int beatsPerMinute) {
        int[] notes = {StdMidi.C4, StdMidi.C4, StdMidi.G4, StdMidi.G4, StdMidi.A4, StdMidi.A4, StdMidi.G4};

        StdMidi.setTempo(beatsPerMinute);

        for (int i = 0; i < notes.length; i++) {
            StdMidi.playNote(notes[i], 1);
        }
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