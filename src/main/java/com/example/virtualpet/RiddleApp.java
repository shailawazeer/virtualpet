package com.example.virtualpet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RiddleApp extends Application {
    private Stage stage;
    private int currentRiddleIndex = 0;
    private List<Riddle> riddles;
    private int score = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.riddles = new ArrayList<>();
        initializeRiddles();

        showRiddle();
        primaryStage.setTitle("Riddles Quiz for Kids");
        primaryStage.show();
    }

    private void initializeRiddles() {
        // Add riddles to the quiz
        riddles.add(new Riddle("What has keys but can’t open locks?", "A piano"));
        riddles.add(new Riddle("What has to be broken before you can use it?", "An egg"));
        riddles.add(new Riddle("I’m tall when I’m young, and I’m short when I’m old. What am I?", "A candle"));
        riddles.add(new Riddle("What begins with T, ends with T, and has T in it?", "A teapot"));
        riddles.add(new Riddle("What has hands but can’t clap?", "A clock"));
        riddles.add(new Riddle("I have branches, but no fruit, trunk, or leaves. What am I?", "A bank"));
        riddles.add(new Riddle("What has a face and two hands but no arms or legs?", "A clock"));
        riddles.add(new Riddle("What is full of holes but still holds water?", "A sponge"));
    }

    private void showRiddle() {
        if (currentRiddleIndex < riddles.size()) {
            Riddle riddle = riddles.get(currentRiddleIndex);
            VBox quizLayout = new VBox(20);
            quizLayout.setStyle("-fx-padding: 50; -fx-alignment: center; -fx-background-color: lightblue;");

            Label riddleLabel = new Label(riddle.getQuestion());
            Button answerButton = new Button("Show Answer");
            answerButton.setOnAction(e -> showAnswer(riddle.getAnswer()));

            quizLayout.getChildren().addAll(riddleLabel, answerButton);
            stage.setScene(new Scene(quizLayout, 400, 300));
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
            score++; // Increase score for viewing the answer.
            showRiddle();
        });

        answerLayout.getChildren().addAll(answerLabel, nextButton);
        stage.setScene(new Scene(answerLayout, 400, 300));
    }

    private void showResult() {
        VBox resultLayout = new VBox(20);
        resultLayout.setStyle("-fx-padding: 50; -fx-alignment: center; -fx-background-color: lightblue;");

        Label resultLabel = new Label("Quiz Complete! Your score: " + score + "/" + riddles.size());
        Button restartButton = new Button("Restart Quiz");
        restartButton.setOnAction(e -> {
            currentRiddleIndex = 0;
            score = 0;
            showRiddle();
        });

        resultLayout.getChildren().addAll(resultLabel, restartButton);
        stage.setScene(new Scene(resultLayout, 400, 300));
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
