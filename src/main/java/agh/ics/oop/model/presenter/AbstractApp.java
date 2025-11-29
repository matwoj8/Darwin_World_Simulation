package agh.ics.oop.model.presenter;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import agh.ics.oop.model.basic.Vector2d;

import java.io.IOException;
import java.util.Objects;

public abstract class AbstractApp extends Application {
    protected FXMLLoader loader;
    protected abstract String getFXMLPath();

    @Override
    public void start(Stage primaryStage) throws IOException {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(getFXMLPath()));
        BorderPane viewRoot = loader.load();

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        primaryStage.getIcons().add(icon);

        configureStage(primaryStage, viewRoot);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Application is closing");
            System.exit(0);
        });
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Animals World");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}

