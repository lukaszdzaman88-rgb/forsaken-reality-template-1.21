package hehex.forsaken.mixin;

import hehex.forsaken.item.IArrowStats;
import hehex.forsaken.item.IWeaponStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @ModifyArg(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private static Entity modifyCrossbowShot(Entity entity) {
        if (entity instanceof PersistentProjectileEntity projectile) {
            float weaponDamage = getWeaponDamage(projectile);

            // 2. Get Arrow Damage
            float arrowDamage = 0f;
            ItemStack arrowStack = projectile.getItemStack();
            if (arrowStack.getItem() instanceof IArrowStats arrowStats) {
                arrowDamage = arrowStats.getArrowDamage();
            }

            // 3. Apply Stats
            projectile.setDamage(weaponDamage + arrowDamage);
            projectile.setCritical(true);
        }
        return entity;
    }

    private static float getWeaponDamage(PersistentProjectileEntity projectile) {
        float weaponDamage = 9.0f; // Vanilla fallback

        // 1. Find the Crossbow
        // Since this method is static, we don't have 'this'.
        // We check the shooter (projectile owner) to see what they are holding.
        if (projectile.getOwner() instanceof LivingEntity shooter) {
            if (shooter.getMainHandStack().getItem() instanceof IWeaponStats stats) {
                weaponDamage = stats.getWeaponDamage();
            } else if (shooter.getOffHandStack().getItem() instanceof IWeaponStats stats) {
                weaponDamage = stats.getWeaponDamage();
            }
        }
        return weaponDamage;
    }
}