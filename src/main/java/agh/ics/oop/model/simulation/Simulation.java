package agh.ics.oop.model.simulation;

import agh.ics.oop.model.basic.Vector2d;
import agh.ics.oop.model.entities.Animal;
import agh.ics.oop.model.basic.Boundary;
import agh.ics.oop.model.entities.Fire;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.statistics.SimulationStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Simulation extends Thread{
    private final List<Animal> animals = new ArrayList<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    private final WorldMap map;
    private final SimulationStatistics stats;
    private final List<Animal> children = new ArrayList<>();
    private final int energy;
    private final int genesNumber;
    private final int energyToReproduce;
    private final int costOfReproduction;
    private final int minGeneMutation;
    private final int maxGeneMutation;
    private final int numberOfSpawningPlants;
    private int day = 0;
    private final int simulationLength;
    private final String behaviorVariant;
    private volatile boolean running = true;
    private final int fireFrequency;
    private final int fireDuration;

    public Simulation(int animalAmount, WorldMap map, int energy, int genesNumber, int energyToReproduce, int costOfReproduction, int minGeneMutation, int maxGeneMutation, int numberOfSpawningPlants, String behaviorVariant, int simulationLength, int fireFrequency, int fireDuration) {
        this.map = map;
        this.energy = energy;
        this.genesNumber = genesNumber;
        this.energyToReproduce = energyToReproduce;
        this.costOfReproduction = costOfReproduction;
        this.minGeneMutation = minGeneMutation;
        this.maxGeneMutation = maxGeneMutation;
        this.numberOfSpawningPlants = numberOfSpawningPlants;
        this.behaviorVariant = behaviorVariant;
        this.simulationLength = simulationLength;
        initializeAnimals(animalAmount);
        this.stats = new SimulationStatistics(map, animals, deadAnimals);
        this.fireFrequency = fireFrequency;
        this.fireDuration = fireDuration;
    }

    private void initializeAnimals(int animalAmount) {
        Boundary worldBoundary = map.getCurrentBounds();
        Random random = new Random();
        for (int i = 0; i < animalAmount; i++) {
            int x = random.nextInt(worldBoundary.upperRight().x());
            int y = random.nextInt(worldBoundary.upperRight().y());
            Vector2d randomPosition = new Vector2d(x, y);
            Animal animal = new Animal(randomPosition, energy, randomGenes(genesNumber), energyToReproduce, costOfReproduction, minGeneMutation, maxGeneMutation, behaviorVariant);
            map.place(animal);
            animals.add(animal);
        }
    }

    private int[] randomGenes(int n) {
        int[] genes = new int[n];
        for(int i = 0; i < n; i++){
            genes[i] = (int)(Math.random() * 8);
        }
        return genes;
    }

    private void breedAnimals() {
        List <Vector2d> positionsReserved = new ArrayList<>();

        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).canBreed()) {
                for (int j = i + 1; j < animals.size(); j++) {
                    if (animals.get(j).canBreed() && animals.get(i).position().equals(animals.get(j).position()) && !positionsReserved.contains(animals.get(i).position())) {
                        List<Animal> strongestAnimals = getTwoStrongestAnimals(animals.get(j));
                        positionsReserved.add(strongestAnimals.get(0).position());
                        Animal parent1 = strongestAnimals.get(0);
                        Animal parent2 = strongestAnimals.get(1);
                        Animal child = parent1.breed(parent2);
                        child.setParents(parent1, parent2);
                        parent1.updateChilds();
                        parent2.updateChilds();
                        map.place(child);
                        children.add(child);
                    }
                }
            }
        }
        animals.addAll(children);
        children.clear();
    }

    private void removeDeadAnimals() {
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getEnergy() <= 0) {
                deadAnimals.add(animals.get(i));
                animals.get(i).setDeath(day);
                map.removeDeadAnimal(animals.get(i));
                animals.remove(animals.get(i));
            }
        }
        if (animals.isEmpty()) {
            pauseSimulation();
        }
    }

    public Animal findStrongestAnimal(Vector2d position, Animal excludeAnimal) {
        Animal strongest = null;

        for (Animal animal : animals) {
            if (animal.position().equals(position) && animal != excludeAnimal) {
                if (strongest == null ||
                        animal.getEnergy() > strongest.getEnergy() ||
                        (animal.getEnergy() == strongest.getEnergy() && (
                                animal.getAge() > strongest.getAge() ||
                                        (animal.getAge() == strongest.getAge() && animal.getChildren() > strongest.getChildren()) ||
                                        (animal.getAge() == strongest.getAge() && animal.getChildren() == strongest.getChildren() && Math.random() <= 0.5)
                        ))) {
                    strongest = animal;
                }
            }
        }

        return strongest;
    }

    public List<Animal> getTwoStrongestAnimals(Animal animal1) {
        Vector2d position = animal1.position();
        Animal strongest1 = findStrongestAnimal(position, animal1);
        Animal strongest2 = findStrongestAnimal(position, strongest1);

        List<Animal> strongestAnimals = new ArrayList<>();
        if (strongest1 != null) {
            strongestAnimals.add(strongest1);
        }
        if (strongest2 != null && strongest2 != strongest1) {
            strongestAnimals.add(strongest2);
        }
        return strongestAnimals;
    }


    public int getDay() {
        return day;
    }

    private void generateGrass() {
        if (map.getFreeSpaceNumber() == 0) {
            return;
        }
        for (int i = 0; i < numberOfSpawningPlants; i++) {
            if (map.getFreeSpaceNumber() == 0) {
                return;
            }
            map.placePlants();
        }
    }

    private void moveAnimals() {
        for (Animal animal : animals) {
            map.move(animal);
        }
    }

    private void eatGrass() {
        for (Animal animal : animals) {
            map.eatGrass(animal);
        }
    }

    public List<Animal> getAnimals() { return animals; }

    public boolean isRunning() {
        return running;
    }

    public void pauseSimulation() {
        running = false;
    }

    public void resumeSimulation() {
        running = true;
        synchronized (this) {
            this.notify();
        }
    }

    public List<Animal> getDeadAnimals() {
        return deadAnimals;
    }

    private void igniteRandomGrass() {
        List<Vector2d> grassPositions = new ArrayList<>(map.getPlants().keySet());
        if (!grassPositions.isEmpty()) {
            Vector2d randomPosition = grassPositions.get(new Random().nextInt(grassPositions.size()));
            map.addFire(new Fire(randomPosition, fireDuration));
        }
    }

    private void spreadFire() {
        map.spreadFire();
    }

    private void updateFire() {
        map.updateFire();
    }

    private void checkIfAnimalStepOnFire() {
        for (Animal animal : animals) {
            if (map.hasFireAt(animal.position())) {
                animal.decreaseEnergy(animal.getEnergy());
            }
        }
    }

    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (this) {
                    while (!running) {
                        this.wait();
                    }
                }
                Thread.sleep(simulationLength);
                stats.updateStatistics(this, map, animals, deadAnimals);
                checkIfAnimalStepOnFire();
                removeDeadAnimals();
                moveAnimals();
                eatGrass();
                breedAnimals();
                generateGrass();
                if (day % fireFrequency == 0 && day != 0) {
                    igniteRandomGrass();
                }
                spreadFire();
                updateFire();
                day++;

                if (animals.isEmpty()) {
                    System.out.println("All animals are dead. Ending simulation.");
                    break;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);}
    }
}
