package net.fabricmc.allseeingeye.holding;

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
