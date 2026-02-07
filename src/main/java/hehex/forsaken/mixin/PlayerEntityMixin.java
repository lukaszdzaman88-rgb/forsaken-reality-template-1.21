package hehex.forsaken.mixin;

import hehex.forsaken.item.ICritMultiplier;
import hehex.forsaken.item.custom.ModScytheItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    // This method is called ONLY when the game calculates critical hit damage.
    // By placing our logic here, we ensure it only runs on crits, without needing to find the elusive "crit" method.
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
    private float modifyCritMultiplier(float constant) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack stack = player.getMainHandStack();

        // 1. Apply Lifesteal (Side Effect)
        if (stack.getItem() instanceof ModScytheItem scythe) {
            float lifesteal = scythe.getLifesteal();
            if (lifesteal > 0) {
                // Heal the player safely before the damage is dealt
                player.heal(lifesteal);
            }
        }

        // 2. Apply Custom Multiplier (Return Value)
        if (stack.getItem() instanceof ICritMultiplier critItem) {
            return critItem.getCritDamageMultiplier();
        }

        // Return vanilla default (1.5F) if no custom behaviour matches
        return constant;
    }
}