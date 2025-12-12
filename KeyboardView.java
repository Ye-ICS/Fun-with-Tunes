import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

class KeyboardView extends HBox {
    static final String[] keyTexts = "QWERTYUASDFGHJZXCVBNM".split("");
    static final String[] keyNotes = "C3,D3,E3,F3,G3,A3,B3,C4,D4,E4,F4,G4,A4,B4,C5,D5,E5,F5,G5,A5,B5".split(",");
    boolean[] noteIsPlaying = new boolean[keyNotes.length];

    KeyboardView() {
        createKeysRow();   
        setStyle("-fx-border-color: cyan");     
        setOnKeyPressed(keyEvent -> noteDownFromKey(keyEvent.getText()));
        setOnKeyReleased(keyEvent -> noteUpFromKey(keyEvent.getText()));
    }

    /**
     * Creates a keyboard layout.
     * @return Node keyboard layout.
     */
    void createKeysRow() {
        for (int i = 0; i < keyNotes.length; i++) {
            Button key = new Button(keyTexts[i] + "\n" + keyNotes[i]);
            key.setStyle("-fx-pref-height: 120; -fx-font-size: 10; -fx-font-weight: bold; -fx-text-fill: black; -fx-text-alignment: center; -fx-background-color: ivory; -fx-padding: 5;");
            getChildren().add(key);
            int noteCode = Notes.getNoteCode(keyNotes[i]);
            key.setOnMousePressed(event -> noteDown(noteCode));
            key.setOnMouseReleased(event -> noteUp(noteCode));
        }
    }

    /**
     * Start note corresponding to pressed key, if not already started.
     * @param keyText Name of key pressed. Eg: Q, W, E, etc.
     */
    void noteDownFromKey(String keyText) {
        // Find key in keyTexts array matching keyText
        for (int i = 0; i < keyTexts.length; i++) {
            if (keyTexts[i].equalsIgnoreCase(keyText) && !noteIsPlaying[i]) {  // if found and not already playing
                int noteCode = Notes.getNoteCode(keyNotes[i]);
                noteDown(noteCode);
                noteIsPlaying[i] = true;   // Mark note as playing
                return;
            }
        }
    }

    /**
     * Stop note corresponding to released key.
     * @param keyText Name of key released. Eg: Q, W, E, etc.
     */
    void noteUpFromKey(String keyText) {
        // Find key in keyTexts array matching keyText
        for (int i = 0; i < keyTexts.length; i++) {
            if (keyTexts[i].equalsIgnoreCase(keyText) && noteIsPlaying[i]) {  // if found and currently playing
                int noteCode = Notes.getNoteCode(keyNotes[i]);
                noteUp(noteCode);
                noteIsPlaying[i] = false;   // Mark note as not playing
                return;
            }
        }
    }

    void noteDown(int noteCode) {
        MainView root = (MainView) getScene().getRoot();
        System.out.println("Note on: " + noteCode);
        StdMidi.setVelocity((int) root.volumeSlider.getValue() * 127 / 100);
        StdMidi.noteOn(noteCode);
    }

    void noteUp(int noteCode) {
        System.out.println("Note off: " + noteCode);
        StdMidi.noteOff(noteCode);
    }
}
