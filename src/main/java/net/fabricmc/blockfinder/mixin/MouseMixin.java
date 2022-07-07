package net.fabricmc.blockfinder.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.blockfinder.BlockFinder;
import net.fabricmc.blockfinder.movement.MovementDirection;
import net.fabricmc.blockfinder.movement.PlayerManipulator;
import net.fabricmc.blockfinder.utils.ProcessType;
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

            if (PlayerManipulator.getCurrentProcess() == ProcessType.ANGULAR_YAW && PlayerManipulator.getHeadMovements() != null && PlayerManipulator.getHeadMovements().size() > 0) {
                //BlockFinder.LOGGER.info("Mod is in control of mouse. Increment: " + PlayerManipulator.getYawIncrement());

                double difference = Math.abs(client.player.getYaw() - PlayerManipulator.getHeadMovements().peek().getDestinationAngle());
               // BlockFinder.LOGGER.info("yaw " + client.player.getYaw() + " PlayerManipulator.getHeadMovements().peek().getDestination().getDegrees() " + PlayerManipulator.getHeadMovements().peek().getDestination().getDegrees() + " difference " + difference);

                if (difference <= 15) {
                    PlayerManipulator.setYawIncrementMultiplier(1);
                }

                client.player.changeLookDirection(PlayerManipulator.getHeadMovements().peek().getDirection() * PlayerManipulator.getYawIncrementMultiplier(),0);

                //BlockFinder.LOGGER.info("PlayerManipulator.mouseInControl: " + PlayerManipulator.lookDirectionInControl + " Player yaw: " + client.player.getYaw() + " Direction to face: " + PlayerManipulator.getDirectionToFace() + " Difference: " + difference);
                if (difference <= PlayerManipulator.getAllowedYawDiscrepancy()) {
                    PlayerManipulator.setCurrentProcess(ProcessType.HORIZONTAL);
                    PlayerManipulator.addDirection(MovementDirection.FORWARD);
                    BlockFinder.LOGGER.info("PlayerManipulator.getDirections: " + PlayerManipulator.getHeadMovements());
                    if (PlayerManipulator.getHeadMovements().size() == 0) {
                        BlockFinder.LOGGER.info("Process finished. Player angle reached: " + client.player.getYaw());
                    } else {
                        PlayerManipulator.setYawIncrementMultiplier(10);
                    }
                }
                return;
            } else {
                //BlockFinder.LOGGER.info("Mod is not in control of mouse");
            }
        }
    }

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void updatePlayerPitch(CallbackInfo info) {
        if (client.player != null && PlayerManipulator.getCurrentProcess() == ProcessType.ANGULAR_PITCH) {
            if (client.player.getPitch() < 90) {
                client.player.setPitch(client.player.getPitch() + 1);
            } else {
                BlockFinder.LOGGER.info("Player reached pitch of: " + client.player.getPitch());
                BlockFinder.LOGGER.info("Determining vertical process type...");
                PlayerManipulator.determineVerticalProcessType();
            }
        }
    }


}
