package hehex.forsaken.mixin;

import hehex.forsaken.item.IProjectileStats;
import hehex.forsaken.item.IWeaponStats;
import hehex.forsaken.item.IModdedProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
public class BowItemMixin implements IWeaponStats {

    // Defaultowe statystyki dla zwykłego łuku (jeśli nie jest custom itemem)
    @Override
    public float getWeaponDamage() { return 8.0f; }
    @Override
    public float getProjectileVelocity() { return 3.0f; } // Vanilla max speed
    @Override
    public float getDrawSpeed() { return 20.0f; }

    @Inject(
            method = "onStoppedUsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void applyCustomBowLogic(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci,
                                     PlayerEntity playerEntity, boolean bl, ItemStack itemStack, int i, float pullProgress,
                                     boolean bl2, ArrowItem arrowItem, PersistentProjectileEntity projectile) {

        // 1. Pobierz statystyki broni
        float weaponDmg = this.getWeaponDamage();
        float maxVelocity = this.getProjectileVelocity();

        if (stack.getItem() instanceof IWeaponStats stats) {
            weaponDmg = stats.getWeaponDamage();
            maxVelocity = stats.getProjectileVelocity();
        }

        // 2. Pobierz statystyki strzały
        float arrowDmg = 4.0f; // Default vanilla arrow damage
        if (arrowItem instanceof IProjectileStats arrowStats) {
            arrowDmg = arrowStats.getProjectileDamage();
        }

        // 3. Oblicz mnożnik naciągu (Etapy: 1/3, 2/3, 100%)
        // pullProgress w MC to zazwyczaj 0.0 do 1.0 (czasem więcej przy buffach)
        float chargeMultiplier;
        if (pullProgress >= 1.0f) {
            chargeMultiplier = 1.0f;
        } else if (pullProgress >= 0.67f) { // ~2/3
            chargeMultiplier = 0.67f; // lub 2.0f/3.0f
        } else if (pullProgress >= 0.33f) { // ~1/3
            chargeMultiplier = 0.33f; // lub 1.0f/3.0f
        } else {
            chargeMultiplier = 0.1f; // Minimalne obrażenia przy ledwo napiętym łuku
        }

        // 4. Kalkulacja finalna: (Broń * Naciąg) + Strzała
        float totalDamage = (weaponDmg * chargeMultiplier) + arrowDmg;

        // 5. Zastosuj do pocisku
        if (projectile instanceof IModdedProjectile moddedProjectile) {
            moddedProjectile.setModdedDamage(totalDamage);
        }

        // 6. Zastosuj Custom Velocity (Zasięg)
        // Nadpisujemy prędkość ustawioną przez vanillę
        // Vanilla robi: pullProgress * 3.0F. My robimy: pullProgress * NASZ_VELOCITY
        projectile.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, pullProgress * maxVelocity, 1.0F);
    }
}