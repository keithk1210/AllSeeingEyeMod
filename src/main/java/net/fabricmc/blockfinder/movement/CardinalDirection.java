package net.fabricmc.blockfinder.movement;

public enum CardinalDirection {

    NORTH(180),
    EAST(-90),
    SOUTH(0),
    WEST(90);

    private int degrees;

    private CardinalDirection(int degrees) {
        this.degrees = degrees;
    }

    public int getDegrees() {
        return this.degrees;
    }

}
