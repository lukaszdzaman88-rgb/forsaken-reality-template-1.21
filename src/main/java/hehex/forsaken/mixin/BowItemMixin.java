package hehex.forsaken.mixin;

import hehex.forsaken.item.IArrowStats;
import hehex.forsaken.item.IWeaponStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BowItem.class)
public class BowItemMixin {

    // CORRECT TARGET: ProjectileEntity is where setVelocity is defined.
    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    private void onSetVelocity(ProjectileEntity projectile, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {

        // 1. Run original velocity logic
        projectile.setVelocity(shooter, pitch, yaw, roll, speed, divergence);

        // 2. We need it to be a PersistentProjectileEntity to set damage/crits
        if (projectile instanceof PersistentProjectileEntity persistentProjectile) {

            // 3. Calculate Pull Progress (speed = progress * 3.0)
            float pullProgress = speed / 3.0F;

            float stageMultiplier = 0.33f; // Stage 1
            boolean isMaxStage = false;

            if (pullProgress >= 0.99f) {
                stageMultiplier = 1.0f; // Stage 3
                isMaxStage = true;
            } else if (pullProgress >= 0.6f) {
                stageMultiplier = 0.66f; // Stage 2
            }

            // 4. Get Weapon Damage
            float weaponDamage = 6.0f; // Default
            if ((Object) this instanceof IWeaponStats weaponStats) {
                weaponDamage = weaponStats.getWeaponDamage();
            }

            // 5. Get Arrow Damage
            float arrowDamage = 0f;
            ItemStack arrowStack = persistentProjectile.getItemStack();
            if (arrowStack.getItem() instanceof IArrowStats arrowStats) {
                arrowDamage = arrowStats.getArrowDamage();
            }

            // 6. Apply Stats
            persistentProjectile.setDamage((weaponDamage * stageMultiplier) + arrowDamage);
            persistentProjectile.setCritical(isMaxStage);
        }
    }
}