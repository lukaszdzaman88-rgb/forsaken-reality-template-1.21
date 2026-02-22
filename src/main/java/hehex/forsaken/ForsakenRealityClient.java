package hehex.forsaken;

import hehex.forsaken.network.ParryPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ForsakenRealityClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        KeyBinding parryKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.forsaken-reality.parry",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                "category.forsaken-reality.combat"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (parryKey.wasPressed()) {
                ClientPlayNetworking.send(new ParryPayload());
            }
        });
    }
}

