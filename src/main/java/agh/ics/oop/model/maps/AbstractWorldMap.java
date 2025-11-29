package agh.ics.oop.model.maps;

import agh.ics.oop.model.basic.Boundary;
import agh.ics.oop.model.basic.MapChangeListener;
import agh.ics.oop.model.basic.Vector2d;
import agh.ics.oop.model.entities.Animal;
import agh.ics.oop.model.entities.Fire;
import agh.ics.oop.model.entities.Grass;
import agh.ics.oop.model.entities.WorldElement;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected Map<Vector2d,Animal> animals = new HashMap<>();
    protected Map<Vector2d, Grass> plants = new HashMap<>();
    protected Map<Vector2d, Fire> fires = new HashMap<>();
    protected ArrayList<MapChangeListener> mapChangeListeners = new ArrayList<>();
    protected Vector2d lowerLeft;
    protected Vector2d upperRight;
    protected UUID id;
    protected int costOfReproduction;
    protected int plantsEnergy;
    protected int fireDuration;

    public AbstractWorldMap(int width, int height, int grassNumber, int costOfReproduction, int plantsEnergy, int fireDuration) {
        this.id = UUID.randomUUID();
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1, height - 1);
        this.plantsEnergy = plantsEnergy;
        generateGrass(grassNumber);
        this.costOfReproduction = costOfReproduction;
        this.fireDuration = fireDuration;
    }

    public boolean canMoveTo(Vector2d position) {
        return position.y() <= upperRight.y() && position.y() >= lowerLeft.y();
    }

    private void generateGrass(int grassNumber) {
        for (int i = 0; i < grassNumber; i++) {
            this.placePlants();
        }
    }

    @Override
    public int getGrassesNumber() {
        return plants.size();
    }

    @Override
    public int getFreeSpaceNumber() {
        int freeSpaceNumber = (upperRight.x() + 1) * (upperRight.y() + 1);
        for (int i = 0; i <= upperRight.x(); i++) {
            for (int j = 0; j <= upperRight.y(); j++) {
                Vector2d position = new Vector2d(i, j);
                if (plants.containsKey(position) || animals.containsKey(position)) {
                    freeSpaceNumber--;
                }
            }
        }
        return freeSpaceNumber;
    }

    @Override
    public void place(Animal animal){
        animals.put(animal.position(), animal);
        mapChanged();
    }

    public void placePlants() {
        if (getFreeSpaceNumber() == 0) {
            return;
        }
        Vector2d grassPosition = getPreferredPosition();
        while (plants.containsKey(grassPosition)) {
            grassPosition = getPreferredPosition();
        }
        plants.put(grassPosition, new Grass(grassPosition));
        mapChanged();
    }

    @Override
    public void eatGrass(Animal animal) {
        Vector2d position = animal.position();
        if (plants.containsKey(position)) {
            animal.gainEnergy(plantsEnergy);
            plants.remove(position);
            mapChanged();
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (this.isOccupied(position)) {
            if (animals.containsKey(position)) {
                return animals.get(position);
            }
        }
        if (fires.containsKey(position)) {
            return fires.get(position);
        }

        return plants.get(position);
    }

    public Set<WorldElement> getElements() { return new HashSet<>(animals.values()); }

    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeft, upperRight);
    }

    public void addMapChangeListener(MapChangeListener listener) {
        mapChangeListeners.add(listener);
    }

    public synchronized void mapChanged() {
        for (MapChangeListener listener : mapChangeListeners) {
            listener.mapChanged(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public Vector2d getPreferredPosition() {
        int preferredHeight = (upperRight.y() - lowerLeft.y() + 1) / 5; // 20% of the map height
        int preferredLowerY = lowerLeft.y() + 2 * preferredHeight;
        int preferredUpperY = upperRight.y() - 2 * preferredHeight;
        double chance = Math.random();
        int x, y;

        if (chance <= 0.8) {
            // Generate position in the preferred area
            x = (int)(Math.random() * (upperRight.x() - lowerLeft.x() + 1)) + lowerLeft.x();
            y = (int)(Math.random() * (preferredUpperY - preferredLowerY + 1)) + preferredLowerY;
        } else {
            // Generate position outside the preferred area
            if (Math.random() < 0.5) {
                // Generate position in the lower non-preferred area
                x = (int)(Math.random() * (upperRight.x() - lowerLeft.x() + 1)) + lowerLeft.x();
                y = (int)(Math.random() * (preferredLowerY - lowerLeft.y() + 1)) + lowerLeft.y();
            } else {
                // Generate position in the upper non-preferred area
                x = (int)(Math.random() * (upperRight.x() - lowerLeft.x() + 1)) + lowerLeft.x();
                y = (int)(Math.random() * (upperRight.y() - preferredUpperY + 1)) + preferredUpperY;
            }
        }

        return new Vector2d(x, y);
    }

    @Override
    public void removeDeadAnimal(Animal animal) {
        animals.remove(animal.position());
        animal.die();
        mapChanged();
    }

    public boolean hasGrassAt(Vector2d position) {
        return plants.containsKey(position);
    }

    public boolean hasFireAt(Vector2d position) {
        return fires.containsKey(position);
    }

    public Vector2d[] getGrassPositions() {
        return plants.keySet().toArray(new Vector2d[0]);
    }

    public void removeGrass(Vector2d position) {
        plants.remove(position);
        mapChanged();
    }

    public Map<Vector2d, Grass> getPlants() {
        return plants;
    }
}