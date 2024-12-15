package com.example.virtualpet;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class PetActionsManager {
    private Pet pet;
    private StackPane roomLayout; // Layout to display the room, pet, and food/game elements

    public PetActionsManager(Pet pet) {
        this.pet = pet;
    }

    public void setRoomLayout(StackPane roomLayout) {
        this.roomLayout = roomLayout; // Inject the room layout
    }

    public void eatFood(Label statusLabel, ProgressBar hungerBar, ProgressBar happinessBar, StackPane roomLayout) {
        pet.feed(); // Update pet stats
        hungerBar.setProgress(pet.getHunger() / 100.0); // Corrected to ProgressBar
        happinessBar.setProgress(pet.getHappiness() / 100.0); // Corrected to ProgressBar
        statusLabel.setText("Your pet is enjoying the food!");

        // Display food and pet
        Image foodImage = new Image(getClass().getResourceAsStream("/pet_food.png"));
        ImageView foodView = new ImageView(foodImage);
        foodView.setFitWidth(100);
        foodView.setFitHeight(100);
        foodView.setTranslateX(50); // Position food near the pet

        Image petImage = new Image(getClass().getResourceAsStream("/pet.png"));
        ImageView petView = new ImageView(petImage);
        petView.setFitWidth(100);
        petView.setFitHeight(100);

        // Clear room layout and add the pet and food
        Platform.runLater(() -> {
            roomLayout.getChildren().clear();
            roomLayout.getChildren().addAll(petView, foodView);
        });

        // Remove the food image after 2 seconds
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate eating time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                roomLayout.getChildren().remove(foodView); // Remove food after eating
                statusLabel.setText("Your pet is satisfied!");
            });
        }).start();
    }

    public void playGameChallenge(Label statusLabel, ProgressBar happinessBar, ProgressBar energyBar) {
        pet.play(); // Update pet stats
        happinessBar.setProgress(pet.getHappiness() / 100.0);
        energyBar.setProgress(pet.getEnergy() / 100.0);
        statusLabel.setText("Your pet is playing!");

        // Display a playful animation
        Image playImage = new Image(getClass().getResourceAsStream("/pet_playing.png"));
        ImageView playView = new ImageView(playImage);
        playView.setFitWidth(100);
        playView.setFitHeight(100);

        Platform.runLater(() -> {
            roomLayout.getChildren().clear();
            roomLayout.getChildren().add(playView);
        });

        // Reset room after playtime
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate playtime
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                roomLayout.getChildren().remove(playView);
                statusLabel.setText("Your pet had fun!");
            });
        }).start();
    }
}
