package com.example.virtualpet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Pet {
        private int hunger;
        private int energy;
        private int happiness;
        final Image petImage = new Image(getClass().getResourceAsStream("/pet.png")); //
        

        public Pet() {
            this.hunger = 50;
            this.energy = 50;
            this.happiness = 50;

        }

        // Getters
        public int getHunger() { return hunger; }
        public int getEnergy() { return energy; }
        public int getHappiness() { return happiness; }
        public javafx.scene.image.Image getPetImage() {

            return petImage;
        }

        // Actions
        public void feed() {
            hunger = Math.min(hunger + 20, 100);
            happiness = Math.min(happiness + 10, 100);
        }

        public void play() {
            energy = Math.max(energy - 10, 0);
            happiness = Math.min(happiness + 20, 100);
        }

        public void rest() {
            energy = Math.min(energy + 30, 100);
        }

        public void decayStats() {
            hunger = Math.max(hunger - 5, 0);
            energy = Math.max(energy - 5, 0);
            happiness = Math.max(happiness - 5, 0);
        }
    public String getMood() {
        if (happiness < 20) {
            return "I'm unhappy.";
        } else if (happiness < 50) {
            return "I'm okay.";
        } else if (happiness < 80) {
            return "I'm happy!";
        } else {
            return "I'm great!";
        }
    }


}


