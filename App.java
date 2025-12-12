import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Create components and containers.
        MainView root = new MainView();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles/default_theme.css");
        stage.setScene(scene);
        stage.show();
    }
    
}
