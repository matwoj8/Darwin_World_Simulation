package agh.ics.oop.model.maps;

import agh.ics.oop.model.basic.Boundary;
import agh.ics.oop.model.basic.MapChangeListener;
import agh.ics.oop.model.basic.Vector2d;
import agh.ics.oop.model.entities.Animal;
import agh.ics.oop.model.entities.WorldElement;
import agh.ics.oop.model.entities.Fire;
import agh.ics.oop.model.entities.Grass;

import java.util.List;
import java.util.Map;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place an animal on the map.
     *
     * @param animal The animal to place on the map.
     */
    void place(Animal animal);

    void eatGrass(Animal animal);

    void removeDeadAnimal(Animal animal);

    int getGrassesNumber();

    int getFreeSpaceNumber();

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    WorldElement objectAt(Vector2d position);

    Boundary getCurrentBounds();

    void addMapChangeListener(MapChangeListener consoleMapDisplay);

    void placePlants();

    boolean hasGrassAt(Vector2d position);

    Vector2d[] getGrassPositions();

    void addFire(Fire fire);

    void spreadFire();

    Map<Vector2d, Grass> getPlants();

    boolean hasFireAt(Vector2d position);

    void updateFire();
}