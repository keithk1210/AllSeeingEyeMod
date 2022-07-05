package net.fabricmc.blockfinder.objects;

import net.fabricmc.blockfinder.movement.CardinalDirection;

public class PlayerHeadMovement {

    private final CardinalDirection destination;
    private final int direction;

    public PlayerHeadMovement(CardinalDirection destination, int direction) {
        this.destination = destination;
        this.direction = direction;

    }

    public CardinalDirection getDestination() {
        return destination;
    }

    public int getDirection() {
        return direction;
    }
    @Override
    public String toString() {
        return "Destination: " + destination + " Direction: " + this.direction;
    }
}
