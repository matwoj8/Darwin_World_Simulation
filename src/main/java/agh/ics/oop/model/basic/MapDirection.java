package agh.ics.oop.model.basic;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH,
    SOUTH_EAST,
    SOUTH_WEST,
    WEST,
    EAST;

    @Override
    public String toString() {
        return switch (this) {
            case EAST -> "Wschód";
            case WEST -> "Zachód";
            case NORTH -> "Północ";
            case NORTH_EAST -> "Północny wschód";
            case NORTH_WEST -> "Północny zachód";
            case SOUTH -> "Południe";
            case SOUTH_EAST -> "Południowy wschód";
            case SOUTH_WEST -> "Południowy zachód";
        };
    }

    public static Vector2d toUnitVector(int direction) {
        return switch (direction) {
            case 2 -> new Vector2d(1, 0);
            case 6 -> new Vector2d(-1, 0);
            case 0 -> new Vector2d(0, 1);
            case 4 -> new Vector2d(0, -1);
            case 1 -> new Vector2d(1, 1);
            case 7 -> new Vector2d(-1, 1);
            case 3 -> new Vector2d(1, -1);
            case 5 -> new Vector2d(-1, -1);
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }
}

