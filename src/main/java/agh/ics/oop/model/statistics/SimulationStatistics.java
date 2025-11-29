package agh.ics.oop.model.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agh.ics.oop.model.entities.Animal;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.simulation.Simulation;

public class SimulationStatistics {
    private int day = 0;
    private int animalAmount;
    private int grassAmount;
    private List<Animal> animals;
    private List<Animal> deadAnimals;
    private WorldMap map;

    public SimulationStatistics(WorldMap map, List<Animal> animals, List<Animal> deadAnimals) {
        this.animals = animals;
        this.deadAnimals = deadAnimals;
        this.map = map;
        this.animalAmount = animals.size();
        this.grassAmount = map.getGrassesNumber();
    }

    public void updateStatistics(Simulation simulation, WorldMap map, List<Animal> animals, List<Animal> deadAnimals){
        this.animals = animals;
        this.deadAnimals = deadAnimals;
        this.map = map;
        this.day = simulation.getDay();
        this.animalAmount = animals.size();
        this.grassAmount = map.getGrassesNumber();
    }

    public int getDay() {
        return day;
    }

    public int getAnimalAmount() {
        this.animalAmount = animals.size();
        return animalAmount;
    }

    public int getGrassAmount() {
        this.grassAmount = map.getGrassesNumber();
        return grassAmount;
    }

    public int getFreeSpace() {
        return map.getFreeSpaceNumber();
    }

    public int getAverageEnergy(){
        int sum = 0;
        for (Animal animal : animals) {
            sum += animal.getEnergy();
        }
        if (animals.isEmpty()) {
            return 0;
        }
        else return sum / animals.size();
    }

    public int getAverageLifeLength(){
        int sum = 0;
        for (Animal deadAnimal : deadAnimals) {
            sum += deadAnimal.getAge();
        }
        if (deadAnimals.isEmpty()) {
            return 0;
        }
        else return sum / deadAnimals.size();
    }

    public double getAverageChildrenNumber(){
        int sum = 0;
        for (Animal animal : animals) {
            sum += animal.getChildren();
        }
        if (animals.isEmpty()) {
            return 0.0;
        }
        else
            return (double) Math.round(((double) sum / (double) animals.size()) * 100) / 100;
    }

    public int[] getPopularGenes() {
        Map<int[], Integer> genotypeFrequency = new HashMap<>();

        for (Animal animal : animals) {
            int[] genes = animal.getGenes();
            genotypeFrequency.put(genes, genotypeFrequency.getOrDefault(genes, 0) + 1);
        }

        int[] mostPopularGenotype = new int[0];
        int maxFrequency = 0;
        for (Map.Entry<int[], Integer> entry : genotypeFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostPopularGenotype = entry.getKey();
            }
        }
        return mostPopularGenotype;
    }
}
