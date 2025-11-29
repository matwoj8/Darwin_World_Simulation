package agh.ics.oop.model.entities;

import agh.ics.oop.model.basic.Vector2d;

public class Fire implements WorldElement {
    private final Vector2d position;
    private int duration;

    public Fire(Vector2d position, int duration) {
        this.position = position;
        this.duration = duration;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getDuration() {
        return duration;
    }

    public void decreaseDuration() {
        this.duration--;
    }

    public boolean isExtinguished() {
        return this.duration <= 0;
    }

    @Override
    public Vector2d position() {
        return position;
    }
}