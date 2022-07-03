package net.fabricmc.example.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.example.ExampleMod;
import net.fabricmc.example.movement.CardinalDirection;
import net.fabricmc.example.movement.MovementDirection;
import net.fabricmc.example.movement.PlayerManipulator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public abstract class MouseMixin {


    @Shadow @Final private MinecraftClient client;

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void updatePlayerFacing(CallbackInfo info) {
        if (client.player != null) {
            //ExampleMod.LOGGER.info("Player is not null");
            if (PlayerManipulator.mouseInControl) {
                ExampleMod.LOGGER.info("Mod is in control of mouse. Increment: " + PlayerManipulator.getYawIncrement());
                client.player.changeLookDirection(PlayerManipulator.getYawIncrement(),0);
                if (Math.abs(client.player.getYaw() - PlayerManipulator.getDirectionToFace()) <= PlayerManipulator.allowedYawDiscrepancy) {
                    ExampleMod.LOGGER.info("Process finished. Player angle reached: " + client.player.getYaw());
                    PlayerManipulator.mouseInControl = false;
                    PlayerManipulator.addDirection(MovementDirection.FORWARD);
                }
                return;
            } else {
                //ExampleMod.LOGGER.info("Mod is not in control of mouse");
            }
        }
    }


}
