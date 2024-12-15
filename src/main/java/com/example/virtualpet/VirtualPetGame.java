package com.example.virtualpet;

public class VirtualPetGame {
    public static void main(String[] args) {
        // Create a pet with initial stats (energy, hunger, happiness)
        Pet pet = new Pet(50, 50, 50);

        // Display the initial stats
        pet.displayStats();

        // Interact with the pet
        pet.sleep();  // Sleep to restore energy
        pet.feed();   // Feed to reduce hunger
        pet.play();   // Play to increase happiness

        // Display the updated stats
        pet.displayStats();

        // Simulate decay over time (if the pet is not interacted with)
        pet.decayStats();
        pet.displayStats();
    }
}
