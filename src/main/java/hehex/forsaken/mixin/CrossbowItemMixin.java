package hehex.forsaken.mixin;

import hehex.forsaken.item.IArrowStats;
import hehex.forsaken.item.IWeaponStats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    // Inject just before the arrow is spawned into the world.
    // We capture 'handStack' (the crossbow) and 'projectileEntity' (the arrow).
    @Inject(method = "shoot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void onShoot(World world, LivingEntity shooter, ItemStack handStack, ItemStack projectileStack, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci, ProjectileEntity projectileEntity) {

        if (projectileEntity instanceof PersistentProjectileEntity projectile) {
            float weaponDamage = 0f;
            float arrowDamage = 0f;

            // 1. Get Weapon Damage
            if (handStack.getItem() instanceof IWeaponStats weaponStats) {
                weaponDamage = weaponStats.getWeaponDamage();
            } else {
                weaponDamage = 9.0f; // Vanilla crossbow fallback
            }

            // 2. Get Arrow Damage
            if (projectileStack.getItem() instanceof IArrowStats arrowStats) {
                arrowDamage = arrowStats.getArrowDamage();
            }

            // 3. Apply Damage (Weapon + Arrow)
            projectile.setDamage(weaponDamage + arrowDamage);

            // 4. Always Crit Particles for Crossbow
            projectile.setCritical(true);
        }
    }
}