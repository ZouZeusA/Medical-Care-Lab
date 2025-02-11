package app.labreservation;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        LoginPage loginpage = new LoginPage();
        loginpage.show(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}