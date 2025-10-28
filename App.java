class App {
    public static void main(String[] args) {
        System.out.println("Hello");

        // Testing:
        int noteCode = Notes.getNoteCode("A4");
        System.out.println("A4 has code " + noteCode);
        StdMidi.playNote(noteCode, 10);

        StdMidi.setInstrument(StdMidi.ACOUSTIC_GRAND_PIANO);
        playTwinkle(30);
    }

    static void playTwinkle(int beatsPerMinute) {
        int[] notes = {StdMidi.C4, StdMidi.C4, StdMidi.G4, StdMidi.G4, StdMidi.A4, StdMidi.A4, StdMidi.G4};
        double[] durations = {1, 1, 1, 1, 1, 1, 2};

        StdMidi.setTempo(beatsPerMinute);

        for (int i = 0; i < notes.length; i++) {
            StdMidi.playNote(notes[i], durations[i]);
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