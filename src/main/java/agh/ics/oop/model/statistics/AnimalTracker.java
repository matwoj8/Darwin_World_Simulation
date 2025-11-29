package agh.ics.oop.model.statistics;

import javafx.scene.control.Label;
import agh.ics.oop.model.entities.Animal;
import java.util.Arrays;

public class AnimalTracker {
    private Animal selectedAnimal;
    private final Label genomeLabel;
    private final Label activeGeneLabel;
    private final Label energyLabel;
    private final Label eatenPlantsLabel;
    private final Label childrenCountLabel;
    private final Label descendantsCountLabel;
    private final Label lifeLengthLabel;
    private final Label deathDayLabel;

    public AnimalTracker(Label genomeLabel, Label activeGeneLabel, Label energyLabel, Label eatenPlantsLabel, Label childrenCountLabel, Label descendantsCountLabel, Label lifeLengthLabel, Label deathDayLabel) {
        this.genomeLabel = genomeLabel;
        this.activeGeneLabel = activeGeneLabel;
        this.energyLabel = energyLabel;
        this.eatenPlantsLabel = eatenPlantsLabel;
        this.childrenCountLabel = childrenCountLabel;
        this.descendantsCountLabel = descendantsCountLabel;
        this.lifeLengthLabel = lifeLengthLabel;
        this.deathDayLabel = deathDayLabel;
    }

    public void selectAnimal(Animal animal) {
        this.selectedAnimal = animal;
        updateAnimalInfo();
    }

    public void updateAnimalInfo() {
        genomeLabel.setText("Gene: " + Arrays.toString(selectedAnimal.getGenes()));
        activeGeneLabel.setText("Active gene: " + selectedAnimal.getGenes()[selectedAnimal.getPointer()]);
        energyLabel.setText("Energy level: " + selectedAnimal.getEnergy());
        eatenPlantsLabel.setText("Eaten plants: " + selectedAnimal.getEatenPlants());
        childrenCountLabel.setText("Number of kids: " + selectedAnimal.getChildren());
        descendantsCountLabel.setText("Offsprings: " + selectedAnimal.getOffspring());
        lifeLengthLabel.setText("Age: " + selectedAnimal.getAge());
        deathDayLabel.setText("Death day: " + selectedAnimal.getDeath());
    }
}
