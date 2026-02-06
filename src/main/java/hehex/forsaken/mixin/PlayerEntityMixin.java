package hehex.forsaken.mixin;

import hehex.forsaken.item.ICritMultiplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
    private float modifyCritMultiplier(float constant) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack stack = player.getMainHandStack();

        // Check if the held item has a custom multiplier
        if (stack.getItem() instanceof ICritMultiplier critItem) {
            return critItem.getCritDamageMultiplier();
        }

        // Return vanilla default (1.5F) for all other items
        return constant;
    }
}
