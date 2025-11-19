import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
        tuneButtonsBox.getChildren().addAll(twinkleBtn, moonlightBtn, userNotesBox, fileText, fileSelectBtn, filePlayBtn);
        presetTunesBox.setCenter(tuneButtonsBox);

        // Reactions (aka callbacks):
        twinkleBtn.setOnAction(event -> playTune(0));
        moonlightBtn.setOnAction(event -> playTune(1));
        userNotesBtn.setOnAction(event -> playUserNotes());
        saveToFileBtn.setOnAction(event -> saveTuneToFile());
        fileSelectBtn.setOnAction(event -> openFileChooser());
        filePlayBtn.setOnAction(event -> playTuneFile());
        
        Scene scene = new Scene(presetTunesBox);
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
        // TODO: Get the user notes and durations from the TextFields, wrap your code in try-catch to verify they are valid. Return early if invalid.
        // TODO: Then, similar to openFileChooser to open file chooser, but use showSaveDialog instead of showOpenDialog. Return early if selected file is null (if user cancels).
        // TODO: Finally, call your completed Tunes.saveToFile to save the notes, durations, and bpm.
    }
}
