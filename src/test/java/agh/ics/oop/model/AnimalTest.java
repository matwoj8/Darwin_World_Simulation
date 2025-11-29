package agh.ics.oop.model;

import agh.ics.oop.model.entities.Animal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import agh.ics.oop.model.basic.Vector2d;

public class AnimalTest {

    private final Vector2d position = new Vector2d(0, 0);
    private final int[] genes = {0, 1, 2, 3, 4, 5, 6, 7};
    private final int energy = 50;
    private final int reproductionEnergyCost = 10;
    private final int energyToReproduce = 20;

    @Test
    public void testCanBreed() {
        Animal animal = new Animal(position, 60, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        assertTrue(animal.canBreed(), "Animal should be able to breed when energy is equal or greater than energyToReproduce.");
    }

    @Test
    public void testGetNextOrientation() {
        Animal animal = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        animal.iteratePointer();
        int nextOrientation = animal.getNextOrientation();
        assertTrue(nextOrientation >= 0 && nextOrientation < 8, "Orientation should be within range 0 to 7.");
    }

    @Test
    public void testGetEnergy() {
        Animal animal = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        assertEquals(energy, animal.getEnergy(), "Energy should be equal to the initial energy.");
    }

    @Test
    public void testGainEnergy() {
        Animal animal = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        animal.gainEnergy(20);
        assertEquals(70, animal.getEnergy(), "Energy should increase by the amount gained.");
    }

    @Test
    public void testIsAt() {
        Animal animal = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        assertTrue(animal.isAt(position), "Animal should be at the given position.");
        assertFalse(animal.isAt(new Vector2d(4, 5)), "Animal should not be at a different position.");
    }

    @Test
    public void testDie() {
        Animal animal = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        animal.die();
        assertFalse(animal.isAlive(), "Animal should be marked as not alive after die() is called.");
    }

    @Test
    public void testUpdateOffspring() {
        Animal parent1 = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        Animal parent2 = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        Animal child = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");

        child.setParents(parent1, parent2);
        child.updateOffspring();

        assertEquals(1, child.getOffspring(), "Child should have updated number of offspring.");
        assertEquals(1, parent1.getOffspring(), "Parent should have updated number of offspring.");
        assertEquals(1, parent2.getOffspring(), "Parent should have updated number of offspring.");
    }

    @Test
    public void testGetPosition() {
        Animal animal = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        assertEquals(position, animal.position(), "Animal's position should be equal to the initial position.");
    }

    @Test
    public void testUpdateChilds() {
        Animal animal = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        animal.updateChilds();
        assertEquals(1, animal.getChildren(), "Number of children should be incremented after updateChilds() is called.");
    }

    @Test
    public void testDecreaseEnergy() {
        Animal animal = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        animal.decreaseEnergy(10);
        assertEquals(40, animal.getEnergy(), "Energy should decrease by the specified amount.");
    }

    @Test
    public void testBreedGenesInheritance() {
        int parent1Energy = 80;
        int parent2Energy = 120;
        Animal parent1 = new Animal(position, parent1Energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");
        Animal parent2 = new Animal(position, parent2Energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "FullPredistination");

        Animal child = parent1.breed(parent2);
        int[] childGenes = child.getGenes();

        System.out.println();

        double totalEnergy = parent1Energy + parent2Energy;
        int expectedCutoff = (int) ((parent1Energy / totalEnergy) * genes.length);

        for (int i = 0; i < expectedCutoff; i++) {
            assertEquals(genes[i], childGenes[i], "Child's gene should match parent 1's gene at index " + i);
        }
        for (int i = expectedCutoff; i < genes.length; i++) {
            assertEquals(genes[i], childGenes[i], "Child's gene should match parent 2's gene at index " + i);
        }
    }

}
