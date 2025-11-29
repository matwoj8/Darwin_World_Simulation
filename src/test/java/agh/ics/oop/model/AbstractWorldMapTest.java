package agh.ics.oop.model;

import agh.ics.oop.model.maps.AbstractWorldMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import agh.ics.oop.model.entities.Animal;
import agh.ics.oop.model.entities.Grass;
import agh.ics.oop.model.maps.Globe;
import agh.ics.oop.model.basic.Vector2d;

public class AbstractWorldMapTest {

    private AbstractWorldMap map;
    private final int mapWidth = 10;
    private final int mapHeight = 10;
    private final int grassNumber = 5;
    private final Vector2d position = new Vector2d(0, 0);
    private final int[] genes = {0, 1, 2, 3, 4, 5, 6, 7};
    private final int energy = 100;
    private final int reproductionEnergyCost = 10;
    private final int energyToReproduce = 50;

    @BeforeEach
    public void setUp() {
        int costOfReproduction = 10;
        int plantsEnergy = 20;
        map = new Globe(mapWidth, mapHeight, grassNumber, costOfReproduction, plantsEnergy,0);
    }

    @Test
    public void testPlaceAnimal() {
        Animal animal = new Animal(position, energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "Random Mutation");
        map.place(animal);
        assertEquals(animal, map.objectAt(position), "Animal should be placed at the specified position.");
    }

    @Test
    public void testPlacePlants() {
        map.placePlants();
        int grassCount = map.getGrassesNumber();
        assertTrue(grassCount > 0, "Grass should be placed on the map.");
    }

    @Test
    public void testEatGrass() {
        Animal animal = new Animal(new Vector2d(5, 5), energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "Random Mutation");
        map.place(animal);
        map.placePlants();
        map.eatGrass(animal);
        assertTrue(animal.getEnergy() > 50, "Animal should gain energy after eating grass.");
    }

    @Test
    public void testRemoveDeadAnimal() {
        Animal animal = new Animal(new Vector2d(5, 5), energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "Random Mutation");
        map.place(animal);
        map.removeDeadAnimal(animal);
        assertFalse(map.isOccupied(new Vector2d(5, 5)), "Dead animal should be removed from the map.");
    }

    @Test
    public void testIsOccupied() {
        Animal animal = new Animal(new Vector2d(4, 4), energy, genes, reproductionEnergyCost, energyToReproduce, -1, -2, "Random Mutation");
        map.place(animal);
        assertTrue(map.isOccupied(new Vector2d(4, 4)), "Position should be occupied by an animal.");

        map.placePlants();
        Vector2d grassPosition = null;
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Vector2d pos = new Vector2d(x, y);
                if (map.objectAt(pos) instanceof Grass) {
                    grassPosition = pos;
                    break;
                }
            }
        }

        System.out.println(map.objectAt(grassPosition));

        assertNotNull(grassPosition, "A grass position should be found.");
        assertNotNull(map.objectAt(grassPosition), "Position should be occupied by grass.");
    }

    @Test
    public void testGetGrassesNumber() {
        assertEquals(grassNumber, map.getGrassesNumber(), "The number of grasses should be equal to the initial grass number.");
    }

    @Test
    public void testGetFreeSpaceNumber() {
        int expectedFreeSpaces = (mapWidth * mapHeight) - grassNumber;
        assertEquals(expectedFreeSpaces, map.getFreeSpaceNumber(), "Free space number should be calculated correctly.");
    }
}

