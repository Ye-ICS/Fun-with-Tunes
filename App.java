import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.Node;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class App extends Application {
    Slider bpmSlider;
    Slider volumeSlider;
    TextField notesBox;
    TextField durationsBox;
    Text fileText;
    File tuneFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Create components and containers.
        BorderPane presetTunesBox = new BorderPane();
        presetTunesBox.setStyle("-fx-border-color: black; -fx-border-width: 2");
        
        Label tunesLabel = new Label("Preset Tunes");
        tunesLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-border-color: blue");
        
        FlowPane tuneButtonsBox = new FlowPane();
        tuneButtonsBox.setStyle("-fx-border-color: green; -fx-padding: 10; -fx-hgap: 20; -fx-vgap: 20");
        Button twinkleBtn = new Button("Twinkle");
        Button moonlightBtn = new Button("Moonlight Sonata");

        VBox userNotesBox = new VBox();
        Label noteLabel = new Label("Custom notes:");
        notesBox = new TextField();
        Label durationsLabel = new Label("Durations:");
        durationsBox = new TextField();
        Button userNotesBtn = new Button("Play Notes");
        Button saveToFileBtn = new Button("Save to File");

        fileText = new Text();
        Button fileSelectBtn = new Button("Open File");
        Button filePlayBtn = new Button("Play File");

        Node keyboard = createKeysRow();        
        
        VBox controlsBox = new VBox();
        controlsBox.setStyle("-fx-border-color: red; -fx-spacing: 20; -fx-padding: 10");
        Label bpmLabel = new Label("Tempo (BPM):");
        bpmSlider = new Slider(10, 480, 80);
        Label volumeLabel = new Label("Volume:");
        volumeSlider = new Slider(0, 100, 70);
        volumeSlider.setMajorTickUnit(50);
        volumeSlider.setShowTickLabels(true);

        // Organize components in containers.
        presetTunesBox.setTop(tunesLabel);
        BorderPane.setAlignment(tunesLabel, Pos.CENTER);
        controlsBox.getChildren().addAll(bpmLabel, bpmSlider, volumeLabel, volumeSlider);
        presetTunesBox.setRight(controlsBox);
        userNotesBox.getChildren().addAll(noteLabel, notesBox, durationsLabel, durationsBox, userNotesBtn, saveToFileBtn);
        tuneButtonsBox.getChildren().addAll(twinkleBtn, moonlightBtn, userNotesBox, fileText, fileSelectBtn, filePlayBtn, keyboard);
        presetTunesBox.setCenter(tuneButtonsBox);

        // Reactions (aka callbacks):
        twinkleBtn.setOnAction(event -> playTune(0));
        moonlightBtn.setOnAction(event -> playTune(1));
        userNotesBtn.setOnAction(event -> playUserNotes());
        saveToFileBtn.setOnAction(event -> saveTuneToFile());
        fileSelectBtn.setOnAction(event -> openFileChooser());
        filePlayBtn.setOnAction(event -> playTuneFile());
        
        Scene scene = new Scene(presetTunesBox);
        scene.getStylesheets().add("styles/default_theme.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Callback method to play user-entered notes.
     */
    void playUserNotes() {
        StdMidi.setTempo((int) bpmSlider.getValue());
        StdMidi.setVelocity((int) volumeSlider.getValue() * 127 / 100);
        String notesLine = notesBox.getText();
        String durationsLine = durationsBox.getText();

        try {
            String[] notes = notesLine.split(",");
            double[] durations = Utils.parseDoubles(durationsLine.split(","));
            Tunes.playNotes(notes, durations);
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid durations.");   // TODO: Show a popup alert.
            return;
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());   // TODO: Show a popup alert.
            return;
        }
    }

    /**
     * Callback method to play a preset tune based on the given tune code.
     * @param tuneCode 0 for Twinkle, 1 for Moonlight Sonata
     */
    void playTune(int tuneCode) {
        int bpm = (int) bpmSlider.getValue();
        int volume = (int) volumeSlider.getValue();
        StdMidi.setVelocity(volume * 127 / 100);
        switch (tuneCode) {
            case 0 -> Tunes.playTwinkle(bpm);
            case 1 -> Tunes.playMoonlightSonata(bpm);
            default -> System.out.println("Invalid tune code");
        }
    }

    /**
     * Callback method to play previously selected tune file.
     */
    void playTuneFile() {
        if (tuneFile == null) {
            System.out.println("No file selected. Openning file chooser instead");
            openFileChooser();
            return;
        }

        Tunes.playFile(tuneFile);
    }

    /**
     * Callback to open file chooser and set `tuneFile` to selected file.
     */
    void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Tune File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            tuneFile = selectedFile;
            fileText.setText(tuneFile.getName());
        }
    }

    /**
     * Callback to open file saver dialog and save current user tune to file.
     */
    void saveTuneToFile() {
        // Get the notes and durations verify they are valid.
        String notesLine = notesBox.getText();
        String durationsLine = durationsBox.getText();
        String[] notes = notesLine.split(",");
        double[] durations;
        
        // TODO: This section deserves its own method in Tunes.java!
        try {
            Tunes.getNoteCodes(notes); // Not using the codes, just verifying notes are valid
            durations = Utils.parseDoubles(durationsLine.split(","));
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid durations.");   // TODO: Show a popup alert.
            return;
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());   // TODO: Show a popup alert.
            return;
        }

        if (durations.length != notes.length) {
            System.err.println("Number of durations must match number of notes."); // TODO: Show a popup alert.
            return;
        }
        
        // Open file chooser to let user choose location and file name.
        FileChooser newfileChooser = new FileChooser();
        File saveFile = newfileChooser.showSaveDialog(null);
        if (saveFile == null) { // User cancelled
            return; 
        }

        // Save to file.
        int bpm = (int) bpmSlider.getValue();
        try {
            Tunes.saveToFile(saveFile, notes, durations, bpm);
        } catch (FileNotFoundException fnfe) {
            System.err.println("Invalid path??? How? " + saveFile.getAbsolutePath());
        }
    }

    /**
     * Creates a keyboard layout.
     * @return Node keyboard layout.
     */
    Node createKeysRow() {
        HBox keysRow = new HBox();  // You may use a different layout if you wish.
        String[] notes = "C3,D3,E3,F3,G3,A3,B3,C4,D4,E4,F4,G4,A4,B4,C5,D5,E5,F5,G5,A5,B5".split(",");

        for (int i = 0; i < notes.length; i++) {
            Button key = new Button(notes[i]);
            key.setStyle("-fx-pref-height: 120; -fx-font-size: 10; -fx-font-weight: bold; -fx-text-fill: black; -fx-background-color: ivory; -fx-padding: 5;");
            keysRow.getChildren().add(key);
            int noteCode = Notes.getNoteCode(notes[i]);
            key.setOnMousePressed(event -> noteDown(noteCode));
            key.setOnMouseReleased(event -> noteUp(noteCode));
        }
        return keysRow;
    }

    void noteDown(int noteCode) {
        System.out.println("Note on: " + noteCode);
        StdMidi.setVelocity((int) volumeSlider.getValue() * 127 / 100);
        StdMidi.noteOn(noteCode);
    }

    void noteUp(int noteCode) {
        System.out.println("Note off: " + noteCode);
        StdMidi.noteOff(noteCode);
    }
}
