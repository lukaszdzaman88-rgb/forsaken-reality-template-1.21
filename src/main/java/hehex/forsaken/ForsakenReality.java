package hehex.forsaken;

import hehex.forsaken.combat.ICombatEntity;
import hehex.forsaken.item.ModItems;
import hehex.forsaken.network.ParryPayload;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForsakenReality implements ModInitializer {
	public static final String MOD_ID = "forsaken-reality";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();

		PayloadTypeRegistry.playC2S().register(ParryPayload.ID, ParryPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(ParryPayload.ID, (payload, context) -> {
			context.server().execute(() -> {
				// Trigger parry for the player
				if (context.player() instanceof ICombatEntity combatPlayer) {
					combatPlayer.startParry();
				}
			});
		});


	}

}