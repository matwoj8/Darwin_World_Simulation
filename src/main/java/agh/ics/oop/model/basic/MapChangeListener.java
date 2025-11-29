package agh.ics.oop.model.basic;

import agh.ics.oop.model.maps.WorldMap;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap);
}