package agh.ics.oop.model.presenter;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import agh.ics.oop.model.basic.Vector2d;
import agh.ics.oop.model.entities.Animal;
import agh.ics.oop.model.entities.Fire;
import agh.ics.oop.model.entities.Grass;
import agh.ics.oop.model.entities.WorldElement;
import agh.ics.oop.model.basic.Boundary;
import agh.ics.oop.model.basic.MapChangeListener;
import agh.ics.oop.model.maps.WorldMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.simulation.SimulationEngine;
import agh.ics.oop.model.statistics.AnimalTracker;
import agh.ics.oop.model.statistics.SimulationStatistics;


public class SimulationPresenter implements MapChangeListener {
    private WorldMap worldMap;
    private Simulation simulation;
    private SimulationEngine simulationEngine;
    private SimulationStatistics stats;
    private AnimalTracker animalTracker;
    private Animal selectedAnimal;
    private int animalsAmount;
    private int spawningPlantsAmount;
    private int animalEnergy;
    private int reproduceReady;
    private int reproduceEnergyCost;
    private int minGeneMutation;
    private int maxGeneMutation;
    private int genomeLength;
    private String behaviorVariant;
    private boolean saveStatistics;
    private boolean animalStatistics = false;
    private int simulationLength;
    private int fireFrequency;
    private int fireDuration;
    @FXML
    private Button startStopButton;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label animalCountLabel;
    @FXML
    private Label grassCountLabel;
    @FXML
    private Label freeSpaceCountLabel;
    @FXML
    private Label mostPopularGeneLabel;
    @FXML
    private Label averageEnergyLabel;
    @FXML
    private Label averageLifeLengthLabel;
    @FXML
    private Label averageChildrenCountLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private Label genomeLabel;
    @FXML
    private Label activeGeneLabel;
    @FXML
    private Label energyLabel;
    @FXML
    private Label eatenPlantsLabel;
    @FXML
    private Label childrenCountLabel;
    @FXML
    private Label descendantsCountLabel;
    @FXML
    private Label lifeLengthLabel;
    @FXML
    private Label deathDayLabel;

    public void setSaveStatistics(boolean saveStatistics) {
        this.saveStatistics = saveStatistics;
    }

    public void setAnimalsAmount(int amount) {
        this.animalsAmount = amount;
    }

    public void setSpawningPlantsAmount(int spawningPlantsAmount) {
        this.spawningPlantsAmount = spawningPlantsAmount;
    }

    public void setAnimalEnergy(int animalEnergy) {
        this.animalEnergy = animalEnergy;
    }

    public void setReproduceReady(int reproduceReady) {
        this.reproduceReady = reproduceReady;
    }

    public void setReproduceEnergyCost(int reproduceEnergyCost) {
        this.reproduceEnergyCost = reproduceEnergyCost;
    }

    public void setMinGeneMutation(int minGeneMutation) {
        this.minGeneMutation = minGeneMutation;
    }

    public void setMaxGeneMutation(int maxGeneMutation) {
        this.maxGeneMutation = maxGeneMutation;
    }

    public void setGenomeLength(int genomeLength) {
        this.genomeLength = genomeLength;
    }

    public void setMap(WorldMap map) {
        this.worldMap = map;
    }

    public void setBehaviorVariant(String behaviorVariant) {
        this.behaviorVariant = behaviorVariant;
    }

    public void setFireFrequency(int fireFrequency) {
        this.fireFrequency = fireFrequency;
    }

    public void setFireDuration(int fireDuration) {
        this.fireDuration = fireDuration;
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @FXML
    public void drawMap() {
        clearGrid();
        Boundary boundary = worldMap.getCurrentBounds();
        drawGrid(boundary);
    }

    private void drawGrid(Boundary boundary) {
        for (int i = boundary.lowerLeft().y(); i <= boundary.upperRight().y(); i++) {
            for (int j = boundary.lowerLeft().x(); j <= boundary.upperRight().x(); j++) {
                Vector2d position = new Vector2d(j, i);
                drawGridCell(position, j - boundary.lowerLeft().x() + 1, boundary.upperRight().y() - i + 1);
            }
        }
    }

    private void drawGridCell(Vector2d position, int column, int row) {
        WorldElement element = worldMap.objectAt(position);
        boolean hasGrass = worldMap.hasGrassAt(position);
        boolean hasFire = worldMap.hasFireAt(position);
        Node node = createNodeForElement(element, hasGrass, hasFire);
        mapGrid.add(node, column, row);
    }

    private Node createNodeForElement(WorldElement element, boolean hasGrass, boolean hasFire) {
        Label label = new Label();
        label.setMinWidth(20);
        label.setMinHeight(20);
        label.setAlignment(Pos.CENTER);

        if (element != null) {
            if (element instanceof Fire) {
                label.setStyle("-fx-background-color: red;");
            } else if (element instanceof Grass) { // Assuming * represents grass
                label.setStyle("-fx-background-color: green;");
            } else if (element instanceof Animal) { // Assuming this is an animal
                Animal animal = (Animal) element;
                Circle circle = getCircle(animal, label);
                label.setGraphic(circle);
                if (hasFire) {
                    label.setStyle("-fx-background-color: red;");
                }
                else if (hasGrass) {
                    label.setStyle("-fx-background-color: green;");
                }
                else {
                    label.setStyle("-fx-background-color: lightgreen;");
                }
                if (animal == selectedAnimal) {
                    label.setStyle("-fx-background-color: blue;");
                }
            }
        } else {
            label.setStyle("-fx-background-color: lightgreen;");
        }
        return label;
    }

    private Circle getCircle(Animal animal, Label label) {
        int energy = animal.getEnergy();
        double energyPercentage = (double) energy / animalEnergy;
        Color color;
        if (energyPercentage <= 0.25) {
            color = Color.BEIGE;
        } else if (energyPercentage <= 0.5) {
            color = Color.BURLYWOOD;
        } else if (energyPercentage <= 0.75) {
            color = Color.BROWN;
        } else {
            color = Color.BLACK;
        }

        Circle circle = new Circle();
        circle.setOnMouseClicked(event -> {
            animalStatistics(animal);
            if (animal == selectedAnimal) {
                label.setStyle("-fx-background-color: blue;");
            }
        });
        circle.setRadius(5); // Set the radius to half of the cell size
        circle.setFill(color); // Set the color to the calculated color
        return circle;
    }

    @Override
    public void mapChanged(WorldMap worldMap) {
        Platform.runLater(() -> {
            drawMap();
            updateStatistics();
            updateSelectedAnimalInfo();
        });
    }

    private void updateStatistics() {
        stats.updateStatistics(simulation, worldMap, simulation.getAnimals(), simulation.getDeadAnimals());
        dayLabel.setText("Day: " + stats.getDay());
        animalCountLabel.setText("Animal count: " + stats.getAnimalAmount());
        grassCountLabel.setText("Grass count: " + stats.getGrassAmount());
        freeSpaceCountLabel.setText("Free space count: " + stats.getFreeSpace());
        mostPopularGeneLabel.setText("Most popular gene: " + Arrays.toString(stats.getPopularGenes()));
        averageEnergyLabel.setText("Average energy: " + stats.getAverageEnergy());
        averageLifeLengthLabel.setText("Average life length: " + stats.getAverageLifeLength());
        averageChildrenCountLabel.setText("Average children count: " + stats.getAverageChildrenNumber());

        if (saveStatistics) {
            saveStatisticsToCSV();
        }
    }

    private void saveStatisticsToCSV() {
        File file = new File("statistics.csv");
        boolean fileExists = file.exists();
        try (FileWriter writer = new FileWriter("statistics.csv", true)) {
            if (!fileExists) {
                writer.append("Day,Animal count,Grass count,Free space count,Most popular gene,Average energy,Average life length,Average children count\n");
            }

            writer.append(String.valueOf(stats.getDay())).append(",");
            writer.append(String.valueOf(stats.getAnimalAmount())).append(",");
            writer.append(String.valueOf(stats.getGrassAmount())).append(",");
            writer.append(String.valueOf(stats.getFreeSpace())).append(",");
            writer.append(Arrays.toString(stats.getPopularGenes())).append(",");
            writer.append(String.valueOf(stats.getAverageEnergy())).append(",");
            writer.append(String.valueOf(stats.getAverageLifeLength())).append(",");
            writer.append(String.valueOf(stats.getAverageChildrenNumber())).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void animalStatistics(Animal animal) {
        if (animalTracker == null) {
            animalTracker = new AnimalTracker(
                    genomeLabel,
                    activeGeneLabel,
                    energyLabel,
                    eatenPlantsLabel,
                    childrenCountLabel,
                    descendantsCountLabel,
                    lifeLengthLabel,
                    deathDayLabel
            );
        }
        if (animal == selectedAnimal) {
            selectedAnimal = null;
        } else {
            selectedAnimal = animal;
            animalTracker.selectAnimal(animal);
            animalStatistics = !animalStatistics;
        }
    }

    private void updateSelectedAnimalInfo() {
        if (animalStatistics) {
            animalTracker.updateAnimalInfo();
        }
    }

    public void setSimulationLength(int simulationLength) {
        this.simulationLength = simulationLength;
    }

    @FXML
    public void onStartStopClicked() {
        try {
            if (simulationEngine == null) {
                this.simulation = new Simulation(animalsAmount, worldMap, animalEnergy, genomeLength, reproduceReady, reproduceEnergyCost, minGeneMutation, maxGeneMutation, spawningPlantsAmount, behaviorVariant, simulationLength, fireFrequency, fireDuration);
                simulationEngine = new SimulationEngine(new ArrayList<>());
                simulationEngine.getSimulations().add(simulation);
                stats = new SimulationStatistics(worldMap, simulation.getAnimals(), simulation.getDeadAnimals());
                simulationEngine.runAsync();
                startStopButton.setText("Stop");
            }
            else if (simulationEngine.isRunning()) {
                simulationEngine.stop();
                startStopButton.setText("Start");
            }
            else {
                simulationEngine.start();
                startStopButton.setText("Stop");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
