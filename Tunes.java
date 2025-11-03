import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Tunes {
    
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
    
    /**
     * Plays notes from a file, where notes are all on one line separated by spaces.
     * @param filename Name of file containing notes.
     * @throws FileNotFoundException 
     */
    static void playFile(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));

        String line = sc.nextLine();
        String[] noteStrings = line.split(" ");
        int[] notes = new int[noteStrings.length];

        // Convert String notes into int note codes
        for (int i = 0; i < noteStrings.length; i++) {
            notes[i] = Notes.getNoteCode(noteStrings[i]);
        }

        // Play notes
        for (int i = 0; i < notes.length; i++) {
            StdMidi.playNote(notes[i], 1);
        }
    }

    static void playNotes(String[] noteStrings, double[] durations) {
        int[] noteCodes = new int[noteStrings.length];

        // Convert note strings into note codes
        for (int i = 0; i < noteCodes.length; i++) {
            noteCodes[i] = Notes.getNoteCode(noteStrings[i]);
        }

        // Play note codes
        for (int i = 0; i < noteCodes.length; i++) {
            StdMidi.playNote(noteCodes[i], durations[i]);
        }
    }

    static void playTwinkle(int beatsPerMinute) {
        StdMidi.setTempo(beatsPerMinute);
        
        // Store notes as strings
        String[] noteStrings = "C4 C4 G4 G4 A4 A4 G4".split(" ");
        double[] durations = {1, 1, 1, 1, 1, 1, 2};
        playNotes(noteStrings, durations);
    }

    static void playMoonlightSonata (int beatsPerMinute) {
        StdMidi.setTempo(beatsPerMinute);

        String[] noteStrings = "R E4 G#4 B4 E5 G#5 B5 E6 B5 G#5 E5 B4 G#4 E4".split(" ");
        double[] durations = {4, 0.5, 0.5, 1, 0.5, 0.5, 1, 2, 0.5, 0.5, 1, 0.5, 0.5, 1, 2};
        playNotes(noteStrings, durations);
    }
}
