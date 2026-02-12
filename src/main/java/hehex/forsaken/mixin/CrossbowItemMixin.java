package hehex.forsaken.mixin;

import hehex.forsaken.item.IProjectileStats;
import hehex.forsaken.item.IWeaponStats;
import hehex.forsaken.item.IModdedProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin implements IWeaponStats {

    @Override
    public float getWeaponDamage() { return 9.0f; } // Kusza jest nieco silniejsza bazowo
    @Override
    public float getProjectileVelocity() { return 3.15f; } // Vanilla default
    @Override
    public float getDrawSpeed() { return 25.0f; }

    // Wstrzykujemy się w metodę shoot, która tworzy pociski
    @Inject(
            method = "shoot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void applyCustomCrossbowLogic(World world, LivingEntity shooter, ItemStack crossbowStack, ItemStack projectileStack, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci, ProjectileEntity projectileEntity) {

        if (projectileEntity instanceof PersistentProjectileEntity persistentProjectile) {
            float weaponDmg = this.getWeaponDamage();
            float velocity = this.getProjectileVelocity();

            if (crossbowStack.getItem() instanceof IWeaponStats stats) {
                weaponDmg = stats.getWeaponDamage();
                velocity = stats.getProjectileVelocity();
            }

            float arrowDmg = 4.0f;
            if (projectileStack.getItem() instanceof IProjectileStats arrowStats) {
                arrowDmg = arrowStats.getProjectileDamage();
            } else if (projectileStack.getItem() == Items.ARROW) {
                arrowDmg = 4.0f;
            }

            // Kusza zawsze strzela z "pełną mocą" (brak charge stages przy strzale)
            float totalDamage = weaponDmg + arrowDmg;

            if (persistentProjectile instanceof IModdedProjectile modded) {
                modded.setModdedDamage(totalDamage);
            }

            // Custom Velocity dla kuszy (ignorujemy parametr speed metody shoot, używamy naszego)
            // Uwaga: Kusza vanilla używa stałej prędkości w argumencie, my ją tu nadpisujemy wektorem
            persistentProjectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, velocity, divergence);
        }
    }
}