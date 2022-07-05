package net.fabricmc.blockfinder.commands;

import net.fabricmc.blockfinder.BlockFinder;
import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.fabricmc.blockfinder.utils.ClickType;

public class ClickCommand {

    public static int runCommand(ClickType type) {
        BlockFinder.LOGGER.info("Inside ClickCommand");
        if (type == ClickType.LEFT) {
            PlayerManipulator.setIsLeftClickDown(true);
        } else if (type == ClickType.RIGHT) {
            PlayerManipulator.setIsRightClickDown(true);
        } else if (type == ClickType.STOP) {
            PlayerManipulator.setIsLeftClickDown(false);
            PlayerManipulator.setIsRightClickDown(true);
        }
        return 1;
    }
}
