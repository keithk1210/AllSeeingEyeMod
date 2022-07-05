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
    private void updatePlayerLookDirection(CallbackInfo info) {
        if (client.player != null) {
            //BlockFinder.LOGGER.info("Player is not null");
            //BlockFinder.LOGGER.info("Player pitch: " + client.player.getPitch());
            if (client.player.getYaw() > 180) { //TODO this is not the best way to do this
                client.player.setYaw(-180);
                BlockFinder.LOGGER.info("player yaw reset to: " + client.player.getYaw());
            } else if (client.player.getYaw() < -180) {
                client.player.setYaw(180);
                BlockFinder.LOGGER.info("player yaw reset to: " + client.player.getYaw());
            }

            if (PlayerManipulator.lookDirectionInControl == true && PlayerManipulator.getDirectionToFace() > -181) {
                //BlockFinder.LOGGER.info("Mod is in control of mouse. Increment: " + PlayerManipulator.getYawIncrement());
                double difference = Math.abs(client.player.getYaw() - PlayerManipulator.getDirectionToFace());
                if (difference <= 15) {
                    PlayerManipulator.setYawIncrementMultiplier(1);
                    BlockFinder.LOGGER.info("Yaw increment multiplier set to: " + PlayerManipulator.getYawIncrementMultiplier());
                }

                client.player.changeLookDirection(PlayerManipulator.getYawIncrement() * PlayerManipulator.getYawIncrementMultiplier(),0);

                //BlockFinder.LOGGER.info("PlayerManipulator.mouseInControl: " + PlayerManipulator.lookDirectionInControl + " Player yaw: " + client.player.getYaw() + " Direction to face: " + PlayerManipulator.getDirectionToFace() + " Difference: " + difference);
                if (difference <= 1) {
                    BlockFinder.LOGGER.info("Process finished. Player angle reached: " + client.player.getYaw());
                    PlayerManipulator.lookDirectionInControl = false;
                    PlayerManipulator.addDirection(MovementDirection.FORWARD);
                }
                return;
            } else {
                //BlockFinder.LOGGER.info("Mod is not in control of mouse");
            }
        }
    }

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void updatePlayerPitch(CallbackInfo info) {
        if (client.player != null && PlayerManipulator.getPitchInControl() == true) {
            if (client.player.getPitch() < 90) {
                client.player.setPitch(client.player.getPitch() + 1);
            } else {
                BlockFinder.LOGGER.info("Player reached pitch of: " + client.player.getPitch());
                PlayerManipulator.setPitchInControl(false);
            }
        }
    }


}
