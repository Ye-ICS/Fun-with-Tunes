import java.util.Scanner;

class App {
    public static void main(String[] args) {
        System.out.println("Hello");

        // Testing:
        playUserNotes();

        StdMidi.setInstrument(StdMidi.ACOUSTIC_GRAND_PIANO);
        playTwinkle(30);
    }

    /**
     * Lets user enter notes all on one line separated by spaces, and plays them back.
     */
    static void playUserNotes() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter each note, separated by spaces: ");
        String line = sc.nextLine();
        String[] noteStrings = line.split(" "); // Split by spaces
        int[] notes = new int[noteStrings.length];

        // Convert user notes into int note codes
        for (int i = 0; i < noteStrings.length; i++) {
            notes[i] = Notes.getNoteCode(noteStrings[i]);
        }

        // Play notes
        for (int i = 0; i < notes.length; i++) {
            StdMidi.playNote(notes[i], 1);
        }
    }

    static void playTwinkle(int beatsPerMinute) {
        // Store notes as strings
        String[] noteStrings = "C4 C4 G4 G4 A4 A4 G4".split(" ");
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