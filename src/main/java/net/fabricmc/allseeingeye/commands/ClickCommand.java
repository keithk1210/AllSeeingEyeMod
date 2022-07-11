package net.fabricmc.allseeingeye.commands;

import net.fabricmc.allseeingeye.AllSeeingEye;
import net.fabricmc.allseeingeye.movement.PlayerManipulator;
import net.fabricmc.allseeingeye.utils.ClickType;

public class ClickCommand {

    public static int runCommand(ClickType type) {
        AllSeeingEye.LOGGER.info("Inside ClickCommand");
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
