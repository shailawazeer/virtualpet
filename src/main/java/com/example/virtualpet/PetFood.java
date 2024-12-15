package com.example.virtualpet;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class PetFood {
    private ImageView foodView;

    // Constructor to initialize the food image
    public PetFood(Pane gameLayout) {
        // Load the pet food image
        Image foodImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pet_food.png")));

        // Check if the image loaded successfully
        if (foodImage.isError()) {
            System.out.println("Error loading pet food image");
        } else {
            System.out.println("Pet food image loaded successfully");
        }

        foodView = new ImageView(foodImage);

        // Set the dimensions of the food image
        foodView.setFitWidth(100);
        foodView.setFitHeight(100);

        // Initially set the position of the food (e.g., in front of the pet)
        foodView.setTranslateX(50); // Adjust this value to match your pet's position
        foodView.setTranslateY(200); // Adjust this value to match the pet's position

        // Make the food initially invisible
        foodView.setVisible(false);

        // Add the food image to the game layout
        gameLayout.getChildren().add(foodView);
    }

    // Method to display the food
    public void showFood() {
        Platform.runLater(() -> {
            foodView.setVisible(true);   // Make the food visible
            foodView.toFront();          // Bring the food image to the front
        });
    }


    // Method to hide the food
    public void hideFood() {
        Platform.runLater(() -> foodView.setVisible(false)); // Ensure visibility change happens on the UI thread
    }
}
