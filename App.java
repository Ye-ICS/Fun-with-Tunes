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
        // Store notes as strings
        String[] noteStrings = {"C4", "C4", "G4", "G4", "A4", "A4", "G4"};
        int[] noteCodes = new int[noteStrings.length];  // 7 elements, {0, 0, 0, 0, 0, 0, 0 }
        double[] durations = {1, 1, 1, 1, 1, 1, 2};

        // Convert note strings into note codes
        for (int i = 0; i < noteCodes.length; i++) {
            noteCodes[i] = Notes.getNoteCode(noteStrings[i]);
        }

        StdMidi.setTempo(beatsPerMinute);
        // Play note codes
        for (int i = 0; i < noteCodes.length; i++) {
            StdMidi.playNote(noteCodes[i], durations[i]);
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