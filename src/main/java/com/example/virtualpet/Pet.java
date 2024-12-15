package com.example.virtualpet;

public class Pet {
    private double energy;
    private double happiness;
    private double hunger;

    public Pet(double energy, double hunger, double happiness) {
        this.energy = energy;
        this.hunger = hunger;
        this.happiness = happiness;
    }

    // Getter and setter methods
    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        // Ensure energy value stays within a valid range (0 to 100)
        this.energy = Math.min(Math.max(energy, 0), 100);
    }

    public double getHappiness() {
        return happiness;
    }

    public void setHappiness(double happiness) {
        // Ensure happiness value stays within a valid range (0 to 100)
        this.happiness = Math.min(Math.max(happiness, 0), 100);
    }

    public double getHunger() {
        return hunger;
    }

    public void setHunger(double hunger) {
        // Ensure hunger value stays within a valid range (0 to 100)
        this.hunger = Math.min(Math.max(hunger, 0), 100);
    }

    // Method to decrease stats over time
    public void decayStats() {
        setEnergy(this.energy - 5);  // Decrease energy by 5 (min 0)
        setHappiness(this.happiness - 3);  // Decrease happiness by 3 (min 0)
        setHunger(this.hunger + 5);  // Increase hunger by 5 (max 100)
    }

    // Feed the pet - increases hunger and happiness, restores energy
    public void feed() {
        setHunger(this.hunger - 20);  // Decrease hunger by 20 (min 0)
        setHappiness(this.happiness + 10);  // Increase happiness by 10 (max 100)
        setEnergy(this.energy + 5);  // Increase energy slightly
    }

    // Play with the pet - increases happiness, decreases energy
    public void play() {
        setHappiness(this.happiness + 15);  // Increase happiness by 15 (max 100)
        setEnergy(this.energy - 10);  // Decrease energy by 10 (min 0)
    }

    // Let the pet sleep - restores energy and decreases hunger
    public void sleep() {
        setEnergy(this.energy + 20);  // Increase energy by 20 (max 100)
        setHunger(this.hunger + 10);  // Increase hunger by 10 (max 100)
    }

    // Display the stats of the pet (for debugging or showing in the UI)
    public void displayStats() {
        System.out.println("Energy: " + this.energy);
        System.out.println("Happiness: " + this.happiness);
        System.out.println("Hunger: " + this.hunger);
    }
}
