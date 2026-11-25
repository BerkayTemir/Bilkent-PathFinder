
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/MainView.fxml") // supa base keyim olmadigi icin buraya MainView.fxml yazdim, degistirilebilir
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Bilkent Pathfinder - Sign Up");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

