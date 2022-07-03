package net.fabricmc.blockfinder.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.blockfinder.BlockFinder;
import net.fabricmc.blockfinder.movement.MovementDirection;
import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public abstract class MouseMixin {


    @Shadow @Final private MinecraftClient client;

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void updatePlayerFacing(CallbackInfo info) {
        if (client.player != null) {
            //BlockFinder.LOGGER.info("Player is not null");
            PlayerManipulator playerManipulator = BlockFinder.getPlayerManipulatorFor(client.player);
            if (playerManipulator.mouseInControl) {
                BlockFinder.LOGGER.info("Mod is in control of mouse. Increment: " + playerManipulator.getYawIncrement());
                client.player.changeLookDirection(playerManipulator.getYawIncrement(),0);
                if (Math.abs(client.player.getYaw() - playerManipulator.getDirectionToFace()) <= playerManipulator.allowedYawDiscrepancy) {
                    BlockFinder.LOGGER.info("Process finished. Player angle reached: " + client.player.getYaw());
                    playerManipulator.mouseInControl = false;
                    playerManipulator.addDirection(MovementDirection.FORWARD);
                }
                return;
            } else {
                //BlockFinder.LOGGER.info("Mod is not in control of mouse");
            }
        }
    }


}
