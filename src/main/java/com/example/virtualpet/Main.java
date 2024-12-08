package com.example.virtualpet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private UIManager uiManager; // Declare the uiManager variable.

    @Override
    public void start(Stage primaryStage) {
        uiManager = new UIManager(primaryStage); // Initialize uiManager.

        // Show the Main Menu Scene when the game starts
        Scene mainMenuScene = uiManager.getMainMenuScene(); // Get the main menu scene
        primaryStage.setTitle("Virtual Pet Game");
        primaryStage.setScene(mainMenuScene); // Set the main menu scene
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
