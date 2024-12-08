package com.example.virtualpet;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameLogic {
    private Pet pet;
    private UIManager uiManager;
    private Timeline timer;

    public GameLogic(Pet pet, UIManager uiManager) {
        this.pet = pet;
        this.uiManager = uiManager;
        setupTimer();
    }

    private void setupTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            pet.decayStats();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }
}
