package sprint2.sprint2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    BorderPane root = new BorderPane();

    Pane settingsRoot = new Pane();
    Scene settingsScene = new Scene(settingsRoot, 500, 500);
    Scene gamePlayScene = new Scene(root, 500,500);
    GUI gui = new GUI(root);

    public Main() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) {

        gui.setPrimaryStage(primaryStage);
        gui.setSettingsScene(settingsScene);
        gui.setGameScene((gamePlayScene));

        gui.SettingsPane(settingsRoot);

        primaryStage.setScene(settingsScene);
        primaryStage.setTitle("SOS Game");
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}


