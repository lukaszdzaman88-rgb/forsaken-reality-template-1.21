package hehex.forsaken.mixin;


import hehex.forsaken.combat.ICombatEntity;
import hehex.forsaken.item.custom.ModMeleeItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    // Prevent attacking while parrying
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void preventAttackWhileParrying(Entity target, CallbackInfo ci) {
        if (((ICombatEntity) this).getParryTicks() > 0) {
            ci.cancel();
        }
    }

    // Modify Critical Damage specifically based on our new Custom Variable
    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
    private float modifyCritDamage(float originalDamage) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // In vanilla logic, `f` (which is stored here) was multiplied by 1.5 if crit.
        // We detect if player is natively falling (vanilla crit condition)
        boolean isCrit = player.fallDistance > 0.0F && !player.isOnGround() && !player.isClimbing() && !player.isTouchingWater() && !player.hasVehicle();

        if (isCrit && player.getMainHandStack().getItem() instanceof ModMeleeItem customWeapon) {
            // Vanilla already applied * 1.5, we reverse it and apply our custom multiplier
            float baseDamage = originalDamage / 1.5f;
            return baseDamage * customWeapon.getCritMultiplier();
        }

        return originalDamage;
    }
}