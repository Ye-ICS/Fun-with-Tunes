import java.util.HashMap;
import java.util.Map;

/**
 * Utility class with methods for processing musical notes.
 */
class Notes {
    private static Map<String, Integer> noteCodesMap = buildNoteCodes();

    private static Map<String, Integer> buildNoteCodes() {
        int offsetC = -9; // offset of C note relative to base A note in its scale.
        int startScale = -1;
        int endScale = 9;
        String[] noteFormatStrings = { "c%d", "c#%d", "d%d", "d#%d", "e%d", "f%d", "f#%d", "g%d", "g#%d", "a%d", "a#%d", "b%d" };

        Map<String, Integer> noteCodesMap = new HashMap<String, Integer>();
        int noteCode = 0;

        for (int scale = startScale; scale <= endScale; scale++) {
            // Frequency of base A note in this scale.
            for (int i = 0; i < noteFormatStrings.length; i++) {
                // Format note.
                String note = String.format(noteFormatStrings[i], scale);
                noteCodesMap.put(note, noteCode);

                // Also add equivalent note, if any.
                String equivalentNote = getEquivalentNote(note);
                if (!equivalentNote.equals(note)) {
                    noteCodesMap.put(equivalentNote.toLowerCase(), noteCode);
                }

                noteCode++;
            }
        }

        return noteCodesMap;
    }
    
    /**
     * Returns alternative note equivalent to given note. Example: C#3 is equivalent of Db3, E#3 is equivalent of F3, etc.
     * @param note Note in the Ln, L#n, or Lbn format, where L is a note letter (A-G) and n is a scale number (0-8).
     * @return Alternative note equivalent to the given note, or the same note if no equivalent alternative.
     */
    static String getEquivalentNote(String note) {
        String noteLowerCase = note.toLowerCase();
        char naturalNote = note.toUpperCase().charAt(0);

        // Special case sharps:
        if (noteLowerCase.startsWith("b#")) {
            var scale = Integer.parseInt(note.substring(2));
            return "C" + (scale + 1);
        }
        if (noteLowerCase.startsWith("e#"))
            return "F" + note.substring(2);
        if (noteLowerCase.startsWith("g#"))
            return "Ab" + note.substring(2);

        // Special case flats:
        if (noteLowerCase.startsWith("cb")) {
            var scale = Integer.parseInt(note.substring(2));
            return "B" + (scale - 1);
        }
        if (noteLowerCase.startsWith("fb"))
            return "E" + note.substring(2);
        if (noteLowerCase.startsWith("ab"))
            return "G#" + note.substring(2);

        // All other sharps:
        if (note.charAt(1) == '#')
            // Return equivalent flat:
            return (char) (naturalNote + 1) + "b" + note.substring(2);

        // All other flats:
        if (note.charAt(1) == 'b') {
            return (char) (naturalNote - 1) + "#" + note.substring(2);
        }

        // Naturals:
        int scale = Integer.parseInt(note.substring(1));
        return switch (naturalNote) {
            case 'C'-> "B#" + (scale - 1);
            case 'E'-> "Fb" + scale;
            case 'F'-> "E#" + scale;
            case 'B'-> "Cb" + (scale + 1);
            default -> note;    // No equivalent, return original note
        };
    }

    /**
     * Returns corresponding int code of specified note in scientific pitch notation.
     * Used in conjuction with StdMidi class from stdlib from Princeton: https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdMidi.html
     * Example note strings: C4 is middle C, C#4 is middle C sharp, Db4 is middle D flat, and so on.
     * @param note Note string in scientific notation, or R, which represents a rest (no sound).
     * @return int Corresponding numeric code of the note, according to https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdMidi.html
     */
    static int getNoteCode(String note) {
        if (note.equalsIgnoreCase("r")) {
            return -1;
        }
        if (!noteCodesMap.containsKey(note.toLowerCase())) {
            throw new IllegalArgumentException("Invalid note string: " + note);
        }
        return noteCodesMap.get(note.toLowerCase());
    }
}
