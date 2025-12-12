import java.io.File;
import java.io.FileNotFoundException;

import javafx.geometry.Pos;
import javafx.scene.Node;
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

class MainView extends BorderPane {
    static final String[] keyTexts = "QWERTYUASDFGHJZXCVBNM".split("");
    static final String[] keyNotes = "C3,D3,E3,F3,G3,A3,B3,C4,D4,E4,F4,G4,A4,B4,C5,D5,E5,F5,G5,A5,B5".split(",");
    Thread backgroundTuneThread = null;
    Slider bpmSlider;
    Slider volumeSlider;
    TextField notesBox;
    TextField durationsBox;
    Text fileText;
    File tuneFile;

    
    MainView() {
       
        setStyle("-fx-border-color: black; -fx-border-width: 2");
        
        Label tunesLabel = new Label("Preset Tunes");
        tunesLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-border-color: blue");
        
        FlowPane tuneButtonsBox = new FlowPane();
        tuneButtonsBox.setStyle("-fx-border-color: green; -fx-padding: 10; -fx-hgap: 20; -fx-vgap: 20");
        Button twinkleBtn = new Button("Twinkle");
        Button moonlightBtn = new Button("Moonlight Sonata");
        Button stopBtn = new Button("Stop Tune");

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

        KeyboardView keyboard = new KeyboardView();
        
        VBox controlsBox = new VBox();
        controlsBox.setStyle("-fx-border-color: red; -fx-spacing: 20; -fx-padding: 10");
        Label bpmLabel = new Label("Tempo (BPM):");
        bpmSlider = new Slider(10, 480, 80);
        Label volumeLabel = new Label("Volume:");
        volumeSlider = new Slider(0, 100, 70);
        volumeSlider.setMajorTickUnit(50);
        volumeSlider.setShowTickLabels(true);

        // Organize components in containers.
        setTop(tunesLabel);
        BorderPane.setAlignment(tunesLabel, Pos.CENTER);
        controlsBox.getChildren().addAll(bpmLabel, bpmSlider, volumeLabel, volumeSlider);
        setRight(controlsBox);
        userNotesBox.getChildren().addAll(noteLabel, notesBox, durationsLabel, durationsBox, userNotesBtn, saveToFileBtn);
        tuneButtonsBox.getChildren().addAll(twinkleBtn, moonlightBtn, stopBtn, userNotesBox, fileText, fileSelectBtn, filePlayBtn, keyboard);
        setCenter(tuneButtonsBox);

        // Reactions (aka callbacks):
        twinkleBtn.setOnAction(event -> playTune(0));
        moonlightBtn.setOnAction(event -> playTune(1));
        stopBtn.setOnAction(event -> stopTune());
        userNotesBtn.setOnAction(event -> playUserNotes());
        saveToFileBtn.setOnAction(event -> saveTuneToFile());
        fileSelectBtn.setOnAction(event -> openFileChooser());
        filePlayBtn.setOnAction(event -> playTuneFile());

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
        if (backgroundTuneThread != null && backgroundTuneThread.isAlive()) {
            System.out.println("A tune is already playing...");
            return;
        }

        int bpm = (int) bpmSlider.getValue();
        int volume = (int) volumeSlider.getValue();
        StdMidi.setVelocity(volume * 127 / 100);
        backgroundTuneThread = new Thread(() -> {
            switch (tuneCode) {
                case 0 -> Tunes.playTwinkle(bpm);
                case 1 -> Tunes.playMoonlightSonata(bpm);
                default -> System.out.println("Invalid tune code");
            }
        });
        backgroundTuneThread.start();
    }

    void stopTune() {
        if (backgroundTuneThread == null) {
            return; // Nothing to stop
        }

        backgroundTuneThread.stop();
        backgroundTuneThread = null;
        StdMidi.allNotesOff();  // Turn off any lingering notes.

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
