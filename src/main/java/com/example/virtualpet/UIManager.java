package com.example.virtualpet;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class UIManager {
    private Pet pet;
    private PetActionsManager actionsManager;
    private ProgressBar energyBar, hungerBar, happinessBar;
    private Label statusLabel;
    private Stage primaryStage;

    public UIManager(Stage primaryStage, Pet pet) {
        this.primaryStage = primaryStage;
        this.pet = pet; // Pet instance passed from the main class
        this.actionsManager = new PetActionsManager(this.pet);
    }

    // Main Menu Scene
    public Scene getMainMenuScene() {
        VBox menuLayout = new VBox(20);
        menuLayout.setStyle("-fx-alignment: center; -fx-padding: 50; -fx-background-color: lightblue;");

        Label title = new Label("Virtual Pet Game");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> primaryStage.setScene(getGameScene()));

        Button instructionsButton = new Button("Instructions");
        instructionsButton.setOnAction(e -> showInstructions());

        Button exitButton = new Button("Exit Game");
        exitButton.setOnAction(e -> Platform.exit());

        menuLayout.getChildren().addAll(title, startGameButton, instructionsButton, exitButton);
        return new Scene(menuLayout, 800, 600);
    }

    private void showInstructions() {
        Stage instructionsStage = new Stage();
        VBox instructionsLayout = new VBox(15);
        instructionsLayout.setStyle("-fx-padding: 20; -fx-background-color: lightyellow; -fx-alignment: center;");

        Label title = new Label("How to Play Virtual Pet Game");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label instruction1 = new Label("1. Feed, play, and make your pet sleep to maintain stats.");
        Label instruction2 = new Label("2. Monitor Energy, Hunger, and Happiness levels.");
        Label instruction3 = new Label("3. Play quizzes to boost energy and happiness.");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> instructionsStage.close());

        instructionsLayout.getChildren().addAll(title, instruction1, instruction2, instruction3, closeButton);
        instructionsStage.setScene(new Scene(instructionsLayout, 400, 300));
        instructionsStage.show();
    }

    // Game Scene
    public Scene getGameScene() {
        StackPane gameLayout = new StackPane();

        // Room Background
        Image roomImage = new Image(getClass().getResourceAsStream("/room.jpeg"));
        ImageView roomBackground = new ImageView(roomImage);
        roomBackground.setFitWidth(800);
        roomBackground.setFitHeight(600);

        // Pet Image
        ImageView petView = new ImageView(new Image(getClass().getResourceAsStream("/pet.png")));
        petView.setFitWidth(150);
        petView.setFitHeight(150);
        petView.setTranslateY(150);

        // Pet Animation
        TranslateTransition petAnimation = new TranslateTransition(Duration.seconds(3), petView);
        petAnimation.setByX(100);
        petAnimation.setCycleCount(TranslateTransition.INDEFINITE);
        petAnimation.setAutoReverse(true);
        petAnimation.play();

        // Left Panel for Stats and Buttons
        VBox leftPanel = new VBox(15);
        leftPanel.setStyle("-fx-padding: 20; -fx-alignment: top-left;");

        energyBar = new ProgressBar(pet.getEnergy() / 100.0);
        hungerBar = new ProgressBar(pet.getHunger() / 100.0);
        happinessBar = new ProgressBar(pet.getHappiness() / 100.0);

        Button feedButton = new Button("Feed");
        feedButton.setOnAction(e -> {
            changePetImageTemporarily(petView, "/pet_eating.png", 3000);
            pet.setHunger(Math.max(pet.getHunger() - 20, 0));
            pet.setHappiness(Math.min(pet.getHappiness() + 10, 100));
            statusLabel.setText("Your pet is eating happily!");
            statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            updateStats();
        });

        Button sleepButton = new Button("Sleep");
        sleepButton.setOnAction(e -> {
            changePetImageTemporarily(petView, "/pet_sleep.png", 3000);
            pet.setEnergy(Math.min(pet.getEnergy() + 20, 100));
            updateStats();
            statusLabel.setText("Your pet is sleeping peacefully.");
            statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        });

        // Happy Button
        Button smileButton = new Button("Smile");
        smileButton.setOnAction(e -> {
            // Pause the pet's animation
            petAnimation.pause();

            // Change the pet's image to happy
            changePetImageTemporarily(petView, "/Happy.png", 3000);

            // Update the happiness stat
            pet.setHappiness(Math.min(pet.getHappiness() + 30, 100));
            updateStats();

            // Update the status label
            statusLabel.setText("Your pet is feeling super happy!");
            statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

            // Resume the animation after the image changes back
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(petAnimation::play);
            }).start();
        });

        leftPanel.getChildren().addAll(
                createLabeledBar("Energy", energyBar),
                createLabeledBar("Hunger", hungerBar),
                createLabeledBar("Happiness", happinessBar),
                feedButton, sleepButton, smileButton
        );

        // Status Label
        statusLabel = new Label("Take care of your pet!");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        StackPane.setAlignment(statusLabel, javafx.geometry.Pos.BOTTOM_CENTER);

        gameLayout.getChildren().addAll(roomBackground, petView, leftPanel, statusLabel);
        return new Scene(gameLayout, 800, 600);
    }

    // Utility: Change Pet Image Temporarily
    private void changePetImageTemporarily(ImageView petView, String imagePath, int durationMillis) {
        Image tempImage = new Image(getClass().getResourceAsStream(imagePath));
        petView.setImage(tempImage);

        new Thread(() -> {
            try {
                Thread.sleep(durationMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> petView.setImage(new Image(getClass().getResourceAsStream("/pet.png"))));
        }).start();
    }
    private void updateStats() {
        // Update Progress Bars
        energyBar.setProgress(pet.getEnergy() / 100.0);
        hungerBar.setProgress(pet.getHunger() / 100.0);
        happinessBar.setProgress(pet.getHappiness() / 100.0);

        // Update Label Colors
        updateLabelColor(energyBar, pet.getEnergy());
        updateLabelColor(hungerBar, pet.getHunger());
        updateLabelColor(happinessBar, pet.getHappiness());
    }
    private void updateLabelColor(ProgressBar bar, double value) {
        String color;

        if (value < 30) {
            color = "red"; // Low level
        } else if (value >= 30 && value < 70) {
            color = "yellow"; // Medium level
        } else {
            color = "white"; // High level
        }

        // Set the label color (assumes the label is a sibling of the bar)
        bar.getParent().getChildrenUnmodifiable()
                .filtered(node -> node instanceof Label)
                .forEach(label -> ((Label) label).setStyle("-fx-text-fill: " + color + ";"));
    }
    private HBox createLabeledBar(String name, ProgressBar bar) {
        Label label = new Label(name);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: white;"); // Default color

        HBox hBox = new HBox(10, label, bar);
        hBox.setStyle("-fx-alignment: center-left;");
        return hBox;
    }
    public void updateEnergyBar(double energy) {
        energyBar.setProgress(energy / 100.0); // Update the energy progress bar
    }

    public void updateHappinessBar(double happiness) {
        happinessBar.setProgress(happiness / 100.0); // Update the happiness progress bar
    }

    public void updateHungerBar(double hunger) {
        hungerBar.setProgress(hunger / 100.0); // Update the hunger progress bar
    }
}
