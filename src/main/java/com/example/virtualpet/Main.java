package com.example.virtualpet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private UIManager uiManager; // Declare the uiManager variable.
    private Pet pet; // Declare the pet object.

    @Override
    public void start(Stage primaryStage) {
        // Initialize the pet object
        pet = new Pet(50, 50, 50); // Set initial stats for the pet

        uiManager = new UIManager(primaryStage, pet); // Initialize uiManager with the pet

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
