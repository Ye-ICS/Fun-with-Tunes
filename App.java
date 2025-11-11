import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Button button = new Button("CLICK ME");
        Scene scene = new Scene(button, 200, 200);
        stage.setScene(scene);
        stage.show();
    }
}
