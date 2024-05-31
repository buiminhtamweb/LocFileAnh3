package com.example.locfileanh3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("read-all-file-name-form-folder.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Phần mềm lọc ảnh");
        stage.setScene(scene);
        stage.show();

        HelloController controller = (HelloController) fxmlLoader.getController();
//        ReadAllFileNameFormFolder controller = (ReadAllFileNameFormFolder) fxmlLoader.getController();
        controller.setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}

