package agh.ics.oop.model.maps;

import agh.ics.oop.model.basic.Vector2d;
import agh.ics.oop.model.entities.Animal;
import agh.ics.oop.model.entities.Fire;

import java.util.ArrayList;
import java.util.List;

public class FireMap extends AbstractWorldMap {
    public FireMap(int width, int height, int grassNumber, int costOfReproduction, int plantsEnergy, int fireDuration) {
        super(width, height, grassNumber, costOfReproduction, plantsEnergy, fireDuration);
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.position();
        Vector2d newPosition = animal.getNextMove();

        if (!canMoveTo(newPosition)) {
            newPosition = oldPosition;
        }

        animals.remove(oldPosition);
        animal.move();
        animal.setPosition(newPosition);
        animals.put(animal.position(), animal);

        mapChanged();
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.y() <= upperRight.y() && position.y() >= lowerLeft.y() && position.x() <= upperRight.x() && position.x() >= lowerLeft.x();
    }

    public void spreadFire() {
        List<Vector2d> firePositions = new ArrayList<>(fires.keySet());
        for (Vector2d position : firePositions) {
            spreadFireToNeighbor(position, new Vector2d(0, 1));  // North
            spreadFireToNeighbor(position, new Vector2d(0, -1)); // South
            spreadFireToNeighbor(position, new Vector2d(1, 0));  // East
            spreadFireToNeighbor(position, new Vector2d(-1, 0)); // West
        }

        mapChanged();
    }

    private void spreadFireToNeighbor(Vector2d position, Vector2d direction) {
        Vector2d neighborPosition = position.add(direction);
        if (isWithinBounds(neighborPosition)) {
            if (hasGrassAt(neighborPosition)) {
                removeGrass(neighborPosition);
                addFire(new Fire(neighborPosition, fireDuration));
            }
        }
    }

    public void addFire(Fire fire) {
        fires.put(fire.getPosition(), fire);
        mapChanged();
    }

    public void updateFire() {
        List<Vector2d> firePositions = new ArrayList<>(fires.keySet());
        for (Vector2d position : firePositions) {
            Fire fire = fires.get(position);
            fire.decreaseDuration();
            if (fire.getDuration() == 0) {
                fires.remove(position);
            }
        }

        mapChanged();
    }

    private boolean isWithinBounds(Vector2d position) {
        return position.precedes(upperRight) && position.follows(lowerLeft);
    }
}
