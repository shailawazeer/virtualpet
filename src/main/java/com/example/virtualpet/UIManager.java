package com.example.virtualpet;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class UIManager {
    private Stage stage;
    private Scene mainMenuScene;
    private Scene gameScene; // Unified game scene
    private Pet pet;
    private GameLogic gameLogic;

    public UIManager(Stage stage) {
        this.stage = stage;
        this.pet = new Pet();
        this.gameLogic = new GameLogic(pet, this);
        setupScenes();
    }

    private void setupScenes() {
        mainMenuScene = createMainMenuScene();
        gameScene = createGameScene(); // Initialize the game scene
    }

    public Scene getMainMenuScene() {
        return mainMenuScene;
    }

    public Scene getGameScene() {
        return gameScene;
    }

    private Scene createMainMenuScene() {
        // Layout for the main menu
        VBox layout = new VBox(20); // Use VBox for vertical alignment
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Welcome to Virtual Pet!");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button startButton = new Button("Start Game");
        Button exitButton = new Button("Exit");

        // Set actions for the buttons
        startButton.setOnAction(e -> {
            if (gameScene != null) {
                stage.setScene(gameScene);
            } else {
                System.out.println("Game scene is not initialized.");
            }
        });

        exitButton.setOnAction(e -> System.exit(0)); // Exit the application

        layout.getChildren().addAll(title, startButton, exitButton);

        return new Scene(layout, 400, 300);
    }

    private Scene createGameScene() {
        try {
            // Load the room image
            Image roomImage = new Image(getClass().getResourceAsStream("/room.jpeg"));
            if (roomImage.isError()) {
                System.out.println("Error loading room image: " + roomImage.isError());
            }
            ImageView roomImageView = new ImageView(roomImage);
            roomImageView.setFitWidth(2000);
            roomImageView.setFitHeight(800);
            roomImageView.setPreserveRatio(true);

            // Create a StackPane to hold the room image
            StackPane roomLayout = new StackPane(roomImageView);
            roomLayout.setPrefSize(800, 600);

            // Load the pet image
            Image petImage = pet.getPetImage();
            if (petImage == null) {
                System.out.println("Error loading pet image.");
            }
            ImageView petImageView = new ImageView(petImage);
            petImageView.setFitWidth(450);
            petImageView.setFitHeight(21750);
            petImageView.setTranslateY(200);
            petImageView.setPreserveRatio(true);


            TranslateTransition moveTransition = new TranslateTransition(Duration.seconds(2), petImageView);
            moveTransition.setByX(200);
            moveTransition.setAutoReverse(true);
            moveTransition.setCycleCount(TranslateTransition.INDEFINITE);

            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), petImageView);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.setAutoReverse(true);
            scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);

            ParallelTransition parallelTransition = new ParallelTransition(moveTransition, scaleTransition);
            parallelTransition.play();


            // Center the pet image in the StackPane
            StackPane.setAlignment(petImageView, Pos.CENTER);
            roomLayout.getChildren().add(petImageView);

            // Create a Label for pet status
            Label statusLabel = new Label(pet.getMood());
            StackPane.setAlignment(statusLabel, Pos.CENTER);
            statusLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: pink;-fx-font-weight: bold; -fx-background-color: white(0, 0, 0, 0.5);");
            statusLabel.setTranslateX(90);
            statusLabel.setTranslateY(40);
            roomLayout.getChildren().add(statusLabel);

            // Create Hunger, Energy, Happiness Labels
            Label hungerLabel = new Label("Hunger");
            hungerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label energyLabel = new Label("Energy");
            energyLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label happinessLabel = new Label("Happiness");
            happinessLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            // Create Progress Bars
            ProgressBar hungerBar = new ProgressBar(pet.getHunger() / 100.0);
            ProgressBar energyBar = new ProgressBar(pet.getEnergy() / 100.0);
            ProgressBar happinessBar = new ProgressBar(pet.getHappiness() / 100.0);

            // Create Buttons
            Button feedButton = new Button("Feed");
            feedButton.setOnAction(e -> {
                pet.feed();
                updateBars(hungerBar, energyBar, happinessBar);
                updateStatusLabel(statusLabel);
                feedButton.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: black;");
            });

            Button playButton = new Button("Play");
            playButton.setOnAction(e -> {
                pet.play();
                updateBars(hungerBar, energyBar, happinessBar);
                updateStatusLabel(statusLabel);
                playButton.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: black;");
            });

            Button restButton = new Button("Rest");
            restButton.setOnAction(e -> {
                pet.rest();
                updateBars(hungerBar, energyBar, happinessBar);
                updateStatusLabel(statusLabel);
                restButton.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: black;");
            });

            // Arrange controls in a VBox
            VBox controls = new VBox(15);
            controls.setAlignment(Pos.TOP_LEFT); // Align the VBox content slightly lower
            controls.setTranslateY(100);
            controls.getChildren().addAll(
                    hungerLabel, hungerBar,
                    energyLabel, energyBar,
                    happinessLabel, happinessBar,
                    feedButton, playButton, restButton
            );

            // Position the controls in the StackPane
            StackPane.setAlignment(controls, Pos.BOTTOM_CENTER);
            roomLayout.getChildren().add(controls); // Add controls to the room layout

            // Return the game scene
            return new Scene(roomLayout, 1000, 800); // Use roomLayout as the scene root

        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions
            return new Scene(new VBox(new Label("Error loading game scene.")), 800, 600);
        }
    }

    public void updateBars(ProgressBar hungerBar, ProgressBar energyBar, ProgressBar happinessBar) {
        hungerBar.setProgress(pet.getHunger() / 100.0);
        energyBar.setProgress(pet.getEnergy() / 100.0);
        happinessBar.setProgress(pet.getHappiness() / 100.0);
    }

    private void updateStatusLabel(Label statusLabel) {
        statusLabel.setText(pet.getMood()); // Update the mood message
    }
}
