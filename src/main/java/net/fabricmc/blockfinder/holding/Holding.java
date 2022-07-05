package net.fabricmc.blockfinder.holding;

import net.minecraft.client.option.KeyBinding;

public class Holding {
    private final KeyBinding key;

    public Holding(KeyBinding key)  {
        this.key = key;
    }

    public KeyBinding getKey() {
        return key;
    }
}
