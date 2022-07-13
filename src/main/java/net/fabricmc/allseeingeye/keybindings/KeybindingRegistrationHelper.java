package net.fabricmc.allseeingeye.keybindings;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import javax.swing.text.JTextComponent;

public class KeybindingRegistrationHelper {

    public KeybindingRegistrationHelper() {}

    public static KeyBinding stopKey;

    public static void register() {

        stopKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.allseeingeye.stop",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.allseingeye.stop"));

    }
}
