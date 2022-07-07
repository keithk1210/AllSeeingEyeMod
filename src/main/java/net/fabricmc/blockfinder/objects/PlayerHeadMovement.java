package net.fabricmc.blockfinder.objects;

import net.fabricmc.blockfinder.movement.CardinalDirection;

public class PlayerHeadMovement {

    private final double destinationAngle;
    private final int direction;

    public PlayerHeadMovement(double destinationAngle, int direction) {
        this.destinationAngle = destinationAngle;
        this.direction = direction;

    }

    public double getDestinationAngle() {
        return destinationAngle;
    }

    public int getDirection() {
        return direction;
    }
    @Override
    public String toString() {
        return "Destination Angle: " + String.format("%.2f",destinationAngle) + " Direction: " + this.direction;
    }
}
