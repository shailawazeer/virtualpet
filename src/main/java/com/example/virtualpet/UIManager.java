package com.example.virtualpet;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private Pet pet;
    private PetActionsManager actionsManager;
    private ProgressBar energyBar, hungerBar, happinessBar;
    private Label statusLabel;
    private Stage primaryStage;

    // For Riddles Quiz
    private List<Riddle> riddles;
    private int currentRiddleIndex = 0;
    private int score = 0;

    public UIManager(Stage primaryStage, Pet pet) {
        this.primaryStage = primaryStage;
        this.pet = pet;
        this.actionsManager = new PetActionsManager(this.pet);
        initializeRiddles(); // Initialize riddles for the quiz
    }

    // Main Menu Scene
    public Scene getMainMenuScene() {
        VBox menuLayout = new VBox(20);
        menuLayout.setStyle("-fx-alignment: center; -fx-padding: 50; -fx-background-color: lightblue;");

        Label title = new Label("Virtual Pet Game");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> primaryStage.setScene(getGameScene()));

        Button riddlesQuizButton = new Button("Riddles Quiz");
        riddlesQuizButton.setOnAction(e -> startRiddlesQuiz());

        Button instructionsButton = new Button("Instructions");
        instructionsButton.setOnAction(e -> showInstructions());

        Button exitButton = new Button("Exit Game");
        exitButton.setOnAction(e -> Platform.exit());

        menuLayout.getChildren().addAll(title, startGameButton, riddlesQuizButton, instructionsButton, exitButton);
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
        leftPanel.setStyle("-fx-padding: 20;-fx-text-fill: white; -fx-alignment: top-left;");

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

    // Riddles Quiz
    private void initializeRiddles() {
        riddles = new ArrayList<>();
        riddles.add(new Riddle("What has keys but can’t open locks?", "A piano"));
        riddles.add(new Riddle("What has to be broken before you can use it?", "An egg"));
        riddles.add(new Riddle("I’m tall when I’m young, and I’m short when I’m old. What am I?", "A candle"));
        riddles.add(new Riddle("What begins with T, ends with T, and has T in it?", "A teapot"));
        riddles.add(new Riddle("What has hands but can’t clap?", "A clock"));
        riddles.add(new Riddle("I have branches, but no fruit, trunk, or leaves. What am I?", "A bank"));
        riddles.add(new Riddle("What has a face and two hands but no arms or legs?", "A clock"));
        riddles.add(new Riddle("What is full of holes but still holds water?", "A sponge"));
    }

    private void startRiddlesQuiz() {
        currentRiddleIndex = 0; // Reset the quiz index
        score = 0; // Reset the score
        showRiddle();
    }

    private void showRiddle() {
        if (currentRiddleIndex < riddles.size()) {
            Riddle riddle = riddles.get(currentRiddleIndex);
            VBox riddleLayout = new VBox(20);
            riddleLayout.setStyle("-fx-padding: 50; -fx-alignment: center; -fx-background-color: lightgreen;");

            Label riddleLabel = new Label(riddle.getQuestion());
            Button answerButton = new Button("Show Answer");
            answerButton.setOnAction(e -> showAnswer(riddle.getAnswer()));

            riddleLayout.getChildren().addAll(riddleLabel, answerButton);
            primaryStage.setScene(new Scene(riddleLayout, 400, 300));
        } else {
            showResult();
        }
    }

    private void showAnswer(String answer) {
        VBox answerLayout = new VBox(20);
        answerLayout.setStyle("-fx-padding: 50; -fx-alignment: center; -fx-background-color: lightyellow;");

        Label answerLabel = new Label("The answer is: " + answer);
        Button nextButton = new Button("Next Riddle");
        nextButton.setOnAction(e -> {
            currentRiddleIndex++;
            showRiddle();
        });

        answerLayout.getChildren().addAll(answerLabel, nextButton);
        primaryStage.setScene(new Scene(answerLayout, 400, 300));
    }

    private void showResult() {
        VBox resultLayout = new VBox(20);
        resultLayout.setStyle("-fx-padding: 50; -fx-alignment: center; -fx-background-color: lightblue;");

        Label resultLabel = new Label("Quiz Complete! Your score: " + score + "/" + riddles.size());
        Button restartButton = new Button("Restart Quiz");
        restartButton.setOnAction(e -> startRiddlesQuiz());

        resultLayout.getChildren().addAll(resultLabel, restartButton);
        primaryStage.setScene(new Scene(resultLayout, 400, 300));
    }

    private HBox createLabeledBar(String label, ProgressBar bar) {
        HBox hbox = new HBox(10);
        Label barLabel = new Label(label);
        hbox.getChildren().addAll(barLabel, bar);
        return hbox;
    }

    private void changePetImageTemporarily(ImageView petView, String imagePath, int duration) {
        Image petImage = new Image(getClass().getResourceAsStream(imagePath));
        petView.setImage(petImage);
        new Thread(() -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            Platform.runLater(() -> petView.setImage(new Image(getClass().getResourceAsStream("/pet.png"))));
        }).start();
    }

    private void updateStats() {
        energyBar.setProgress(pet.getEnergy() / 100.0);
        hungerBar.setProgress(pet.getHunger() / 100.0);
        happinessBar.setProgress(pet.getHappiness() / 100.0);
    }

    // Inner class for Riddle
    private static class Riddle {
        private String question;
        private String answer;

        public Riddle(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}