package net.fabricmc.allseeingeye.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.allseeingeye.AllSeeingEye;
import net.fabricmc.allseeingeye.movement.MovementDirection;
import net.fabricmc.allseeingeye.movement.PlayerManipulator;
import net.minecraft.client.input.KeyboardInput;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(KeyboardInput.class)
public class KeyboardMixin {

	@Redirect(method = "tick", at = @At(
			value = "FIELD",
			target = "net/minecraft/client/input/KeyboardInput.pressingForward:Z",
			opcode = Opcodes.GETFIELD,
			ordinal = 0))
	private boolean onPressingForward(KeyboardInput input) {
		input.pressingForward = input.pressingForward || PlayerManipulator.hasDirection(MovementDirection.FORWARD);
		return input.pressingForward;
	}

	@Redirect(method = "tick", at = @At(
			value = "FIELD",
			target = "net/minecraft/client/input/KeyboardInput.pressingBack:Z",
			opcode = Opcodes.GETFIELD,
			ordinal = 0))
	private boolean onPressingBack(KeyboardInput input) {
		input.pressingBack = input.pressingBack;
		return !PlayerManipulator.hasDirection(MovementDirection.FORWARD) && input.pressingBack ;
	}

	@Redirect(method = "tick", at = @At(
			value = "FIELD",
			target = "net/minecraft/client/input/KeyboardInput.pressingLeft:Z",
			opcode = Opcodes.GETFIELD,
			ordinal = 0))
	private boolean onPressingLeft(KeyboardInput input) {
		input.pressingLeft = input.pressingLeft;
		return !PlayerManipulator.hasDirection(MovementDirection.FORWARD) && input.pressingLeft;
	}

	@Redirect(method = "tick", at = @At(
			value = "FIELD",
			target = "net/minecraft/client/input/KeyboardInput.pressingRight:Z",
			opcode = Opcodes.GETFIELD,
			ordinal = 0))
	private boolean onPressingRight(KeyboardInput input) {
		input.pressingRight = input.pressingRight;
		return !PlayerManipulator.hasDirection(MovementDirection.FORWARD) && input.pressingRight;
	}

}