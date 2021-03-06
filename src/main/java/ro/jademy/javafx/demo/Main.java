package ro.jademy.javafx.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        primaryStage.setTitle("ContactList APP");

        Scene scene = new Scene(root, 1024, 640);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
