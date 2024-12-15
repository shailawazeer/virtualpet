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
    private StackPane roomLayout;
    private ProgressBar energyBar, hungerBar, happinessBar;
    private Label statusLabel;
    private Stage primaryStage;
    private String currentQuestion;
    private String correctAnswer;

    public UIManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.pet = pet; // Use the pet instance passed in constructor
        this.actionsManager = new PetActionsManager(this.pet);
    }

    // Main Menu Scene
    public Scene getMainMenuScene() {
        VBox menuLayout = new VBox(20);
        menuLayout.setStyle("-fx-alignment: center; -fx-padding: 50; -fx-background-color: lightblue;");

        Label title = new Label("Virtual Pet Game");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> primaryStage.setScene(getGameScene()));  // Change scene to the game scene

        Button instructionsButton = new Button("Instructions");
        instructionsButton.setOnAction(e -> showInstructions());

        Button exitButton = new Button("Exit Game");
        exitButton.setOnAction(e -> Platform.exit());

        menuLayout.getChildren().addAll(title, startGameButton, instructionsButton, exitButton);
        return new Scene(menuLayout, 800, 600);
    }

    private void showInstructions() {
        // Implement instructions if needed
    }

    // Game Scene
    public Scene getGameScene() {
        // Create the main layout for the game
        StackPane gameLayout = new StackPane();

        // Set the room background image
        Image roomImage = new Image(getClass().getResourceAsStream("/room.jpeg"));
        ImageView roomBackground = new ImageView(roomImage);
        roomBackground.setFitWidth(800);
        roomBackground.setFitHeight(600);

        // Add the pet image
        Image petImage = new Image(getClass().getResourceAsStream("/pet.png"));
        ImageView petView = new ImageView(petImage);
        petView.setFitWidth(150);
        petView.setFitHeight(150);
        petView.setTranslateX(50);
        petView.setTranslateY(150);

        // Add animation for the pet to move back and forth
        TranslateTransition petAnimation = new TranslateTransition(Duration.seconds(3), petView);
        petAnimation.setByX(100);
        petAnimation.setCycleCount(TranslateTransition.INDEFINITE);
        petAnimation.setAutoReverse(true);
        petAnimation.play();

        // Create an instance of PetFood
        PetFood petFood = new PetFood(gameLayout);

        // Left panel for stats and actions
        VBox leftPanel = new VBox(15); // Vertical layout with spacing
        leftPanel.setStyle("-fx-padding: 20; -fx-alignment: top-left;"); // Align panel contents to the left

        // Create labels and progress bars for pet stats
        VBox energyBox = createProgressBarWithLabel("Energy Level", pet.getEnergy());
        Button energyLevelButton = new Button("Check Energy");
        energyLevelButton.setOnAction(e -> statusLabel.setText("Current Energy: " + pet.getEnergy() + "%"));

        VBox hungerBox = createProgressBarWithLabel("Hunger Level", pet.getHunger());
        Button hungerLevelButton = new Button("Check Hunger");
        hungerLevelButton.setOnAction(e -> statusLabel.setText("Current Hunger: " + pet.getHunger() + "%"));

        VBox happinessBox = createProgressBarWithLabel("Happiness Level", pet.getHappiness());
        Button happinessLevelButton = new Button("Check Happiness");
        happinessLevelButton.setOnAction(e -> statusLabel.setText("Current Happiness: " + pet.getHappiness() + "%"));

        // Feed button - Add the energy and happiness updates here
        Button feedButton = new Button("Feed");
        feedButton.setOnAction(e -> {
            // Pause the pet's animation (stop it from moving while eating)
            petAnimation.pause();

            petFood.showFood(); // Show the food in front of the pet
            actionsManager.eatFood(statusLabel, hungerBar, happinessBar, gameLayout);

            // Increase the energy and happiness levels when the pet is fed
            pet.setEnergy(Math.min(pet.getEnergy() + 10, 100));  // Increase energy (max 100)
            pet.setHappiness(Math.min(pet.getHappiness() + 10, 100));  // Increase happiness (max 100)

            // Update progress bars after feeding the pet
            energyBar.setProgress(pet.getEnergy() / 100.0);  // Update energy progress bar
            happinessBar.setProgress(pet.getHappiness() / 100.0);  // Update happiness progress bar

            // Optional: Hide food after a delay (e.g., 3 seconds)
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // 3 seconds for the pet to "eat"
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(() -> {
                    petFood.hideFood();  // Hide food after eating
                    petAnimation.play(); // Resume the pet's movement after feeding
                });
            }).start();
        });

        // **Sleep button - Pauses the pet and increases its energy**
        Button sleepButton = new Button("Sleep");
        sleepButton.setOnAction(e -> {
            // Pause the pet's animation (stop it from moving while sleeping)
            petAnimation.pause();

            // Increase energy and update progress bar
            pet.setEnergy(Math.min(pet.getEnergy() + 20, 100));  // Increase energy by 20 (max 100)
            energyBar.setProgress(pet.getEnergy() / 100.0);  // Update energy progress bar
            statusLabel.setText("Pet is sleeping... Energy increased!");

            // Simulate the sleep duration (e.g., 3 seconds)
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // 3 seconds for the pet to "sleep"
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(() -> {
                    petAnimation.play(); // Resume the pet's movement after sleep
                    statusLabel.setText("Pet is awake and moving!");
                });
            }).start();
        });

        // Inside getGameScene(), add the Play Quiz button:
        Button playQuizButton = new Button("Play Quiz");
        playQuizButton.setOnAction(e -> showQuiz()); // This triggers the quiz
        leftPanel.getChildren().add(playQuizButton); // Add to the layout

        // Add the feed button, sleep button, and others to the left panel
        leftPanel.getChildren().addAll(
                energyBox, energyLevelButton,
                hungerBox, hungerLevelButton,
                happinessBox, happinessLevelButton,
                feedButton, sleepButton // Add sleep button here
        );

        // Status Label at the bottom
        statusLabel = new Label("Welcome to the Virtual Pet Game!");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10;");
        StackPane.setAlignment(statusLabel, javafx.geometry.Pos.BOTTOM_CENTER);

        // Add all elements to the game layout
        gameLayout.getChildren().addAll(roomBackground, petView, leftPanel, statusLabel);

        // Return the game scene
        return new Scene(gameLayout, 800, 600);
    }

    private VBox createProgressBarWithLabel(String labelName, double value) {
        ProgressBar progressBar = new ProgressBar(value / 100.0);
        progressBar.setPrefWidth(150);
        progressBar.setStyle("-fx-accent: green;");

        Label label = new Label(labelName);
        label.setStyle("-fx-font-size: 14px;");

        VBox container = new VBox(5);
        container.getChildren().addAll(label, progressBar);
        container.setStyle("-fx-alignment: center-left;");
        return container;
    }

    private void showQuiz() {
        // Create a new stage for the quiz
        StackPane quizLayout = new StackPane();
        Random rand = new Random();

        // Generate a random question and answers
        int num1 = rand.nextInt(10) + 1;
        int num2 = rand.nextInt(10) + 1;
        currentQuestion = "What is " + num1 + " + " + num2 + "?";
        correctAnswer = String.valueOf(num1 + num2);

        Label questionLabel = new Label(currentQuestion);
        questionLabel.setStyle("-fx-font-size: 20px;");

        Button correctAnswerButton = new Button(correctAnswer);
        correctAnswerButton.setOnAction(e -> {
            pet.setEnergy(80);  // Set energy to 80
            pet.setHappiness(50);  // Set happiness to 50
            statusLabel.setText("Correct! Energy increased.");
            closeQuiz(); // Close the quiz after answering correctly
        });

        // Generate a wrong answer button randomly
        int wrongAnswer = Integer.parseInt(correctAnswer) + rand.nextInt(5) + 1; // Ensure it's not the correct answer
        Button wrongAnswerButton = new Button(String.valueOf(wrongAnswer));
        wrongAnswerButton.setOnAction(e -> {
            statusLabel.setText("Incorrect. Try again.");
            closeQuiz(); // Close the quiz after answering incorrectly
        });

        VBox quizBox = new VBox(20);
        quizBox.getChildren().addAll(questionLabel, correctAnswerButton, wrongAnswerButton);
        quizLayout.getChildren().add(quizBox);

        Scene quizScene = new Scene(quizLayout, 400, 300);
        Stage quizStage = new Stage();
        quizStage.setTitle("Quiz Challenge");
        quizStage.setScene(quizScene);
        quizStage.show();
    }

    // Method to close the quiz and return to the main game screen
    private void closeQuiz() {
        Stage stage = (Stage) primaryStage.getOwner();  // Get the quiz stage
        stage.close(); // Close the quiz window
    }

    public void updateEnergyBar(double energy) {
        energyBar.setProgress(energy / 100.0);
    }

    public void updateHungerBar(double hunger) {
        hungerBar.setProgress(hunger / 100.0);
    }

    public void updateHappinessBar(double happiness) {
        happinessBar.setProgress(happiness / 100.0);
    }
}
