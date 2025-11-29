package agh.ics.oop.model.maps;

import agh.ics.oop.model.basic.Vector2d;
import agh.ics.oop.model.entities.Animal;
import agh.ics.oop.model.entities.Fire;

public class Globe extends AbstractWorldMap{
    public Globe(int width, int height, int grassNumber, int costOfReproduction, int plantsEnergy, int fireDuration) {
        super(width, height, grassNumber, costOfReproduction, plantsEnergy, fireDuration);
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.position();
        Vector2d newPosition = animal.getNextMove();
        if(!canMoveTo(newPosition)) {
            animal.iteratePointer();
        }
        else if (newPosition.x() < lowerLeft.x()) {
            animal.move();
            animals.remove(oldPosition);
            animal.setPosition(new Vector2d(upperRight.x(), newPosition.y()));
            animals.put(animal.position(), animal);
        }
        else if (newPosition.x() > upperRight.x()) {
            animal.move();
            animals.remove(oldPosition);
            animal.setPosition(new Vector2d(lowerLeft.x(), newPosition.y()));
            animals.put(animal.position(), animal);
        }
        else {
            animal.move();
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
            mapChanged();
        }
    }

    public void addFire(Fire fire) {

    }

    public void spreadFire() {

    }

    public void updateFire() {
    }
}
