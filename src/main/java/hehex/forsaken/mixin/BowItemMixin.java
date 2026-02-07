package hehex.forsaken.mixin;

import hehex.forsaken.item.IArrowStats;
import hehex.forsaken.item.IWeaponStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BowItem.class)
public class BowItemMixin {

    // Target 'spawnEntity' using its INTERMEDIARY name (method_8629).
    // remap = false allows this to work even if the RefMap is missing in dev.
    @ModifyArg(method = "onStoppedUsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_8629(Lnet/minecraft/entity/Entity;)Z", remap = false))
    private Entity modifyArrowStats(Entity entity) {
        if (entity instanceof PersistentProjectileEntity projectile) {
            double velocity = projectile.getVelocity().length();
            float pullProgress = (float) (velocity / 3.0);

            float stageMultiplier = 0.33f;
            boolean isMaxStage = false;

            if (pullProgress >= 0.95f) {
                stageMultiplier = 1.0f;
                isMaxStage = true;
            } else if (pullProgress >= 0.6f) {
                stageMultiplier = 0.66f;
            }

            float weaponDamage = 6.0f; // Vanilla Bow Default
            // If this code runs for ModBowItem (and we didn't override), this gets the custom stats
            if ((Object) this instanceof IWeaponStats weaponStats) {
                weaponDamage = weaponStats.getWeaponDamage();
            }

            float arrowDamage = 0f;
            ItemStack arrowStack = projectile.getItemStack();
            if (arrowStack.getItem() instanceof IArrowStats arrowStats) {
                arrowDamage = arrowStats.getArrowDamage();
            }

            projectile.setDamage((weaponDamage * stageMultiplier) + arrowDamage);
            projectile.setCritical(isMaxStage);
        }
        return entity;
    }
}