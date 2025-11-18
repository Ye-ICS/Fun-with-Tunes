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
    TextField noteBox;
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
        Label noteLabel = new Label("Custom note:");
        noteBox = new TextField();
        Button userNotesBtn = new Button("Play Note");
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
        tuneButtonsBox.getChildren().addAll(twinkleBtn, moonlightBtn, noteLabel, noteBox, userNotesBtn, fileText, fileSelectBtn, filePlayBtn);
        presetTunesBox.setCenter(tuneButtonsBox);

        // Reactions (aka callbacks):
        twinkleBtn.setOnAction(event -> playTune(0));
        moonlightBtn.setOnAction(event -> playTune(1));
        userNotesBtn.setOnAction(event -> playUserNote());
        fileSelectBtn.setOnAction(event -> openFileChooser());
        // TODO: Set callback on filePlayBtn to play tuneFile
        
        Scene scene = new Scene(presetTunesBox);
        stage.setScene(scene);
        stage.show();
    }

    void playUserNote() {
        StdMidi.setVelocity((int) volumeSlider.getValue() * 127 / 100);
        String note = noteBox.getText();
        int noteCode = Notes.getNoteCode(note);
        StdMidi.playNote(noteCode, 1);
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

    void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Tune File");
        tuneFile = fileChooser.showOpenDialog(null);

        if (tuneFile != null) {
            fileText.setText(tuneFile.getName());
        }
    }
}
