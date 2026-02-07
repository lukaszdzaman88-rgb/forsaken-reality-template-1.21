package hehex.forsaken.mixin;

import hehex.forsaken.item.ICritMultiplier;
import hehex.forsaken.item.custom.ModScytheItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    // 1. Modify the Crit Damage Multiplier (Vanilla 1.5x -> Custom)
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
    private float modifyCritMultiplier(float constant) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack stack = player.getMainHandStack();

        if (stack.getItem() instanceof ICritMultiplier critItem) {
            return critItem.getCritDamageMultiplier();
        }
        return constant;
    }

    // 2. Lifesteal Logic
    // Using the full signature "crit(Lnet/minecraft/entity/Entity;)V" is safer
    @Inject(method = "crit(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
    private void onCrit(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack stack = player.getMainHandStack();

        // Check if holding a Scythe and apply healing
        if (stack.getItem() instanceof ModScytheItem scythe) {
            float lifesteal = scythe.getLifesteal();
            if (lifesteal > 0) {
                player.heal(lifesteal);
            }
        }
    }
}