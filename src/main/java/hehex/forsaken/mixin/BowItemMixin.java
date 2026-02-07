package hehex.forsaken.mixin;

import hehex.forsaken.item.IArrowStats;
import hehex.forsaken.item.IWeaponStats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public class BowItemMixin {

    // Instead of looking for a specific line (which fails), we intercept the START of the method
    // and CANCEL the vanilla code, running our own logic instead.
    @Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
    public void onStoppedUsingOverwrite(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }

        boolean hasProjectiles = playerEntity.getAbilities().creativeMode || !playerEntity.getProjectileType(stack).isEmpty();
        if (!hasProjectiles) {
            // If we don't have arrows, we still want to cancel vanilla behavior to prevent ghost fires
            // But usually vanilla checks this too. We let vanilla handle the return if we didn't cancel yet?
            // No, if we cancel, we must do everything.
            ci.cancel();
            return;
        }

        // 1. Calculate Pull Progress
        // We can access 'this' as BowItem
        int i = ((BowItem)(Object)this).getMaxUseTime(stack, user) - remainingUseTicks;
        float pullProgress = BowItem.getPullProgress(i);

        // 2. Fire Logic
        if (!((double)pullProgress < 0.1)) {
            ItemStack arrowStack = playerEntity.getProjectileType(stack);
            if (arrowStack.isEmpty()) {
                arrowStack = new ItemStack(Items.ARROW);
            }

            if (world instanceof net.minecraft.server.world.ServerWorld) {
                ArrowItem arrowItem = (ArrowItem)(arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);
                PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, arrowStack, playerEntity, stack);

                // 3. Stats & Velocity
                // Use vanilla velocity (pullProgress * 3.0F).
                persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, pullProgress * 3.0F, 1.0F);

                // --- CUSTOM DAMAGE LOGIC START ---
                float stageMultiplier = 0.33f;
                if (pullProgress >= 1.0f) {
                    stageMultiplier = 1.0f;
                } else if (pullProgress >= 0.6f) {
                    stageMultiplier = 0.66f;
                }

                // Get Weapon Damage
                float weaponDamage = 6.0f; // Default for Vanilla Bow
                if (stack.getItem() instanceof IWeaponStats weaponStats) {
                    weaponDamage = weaponStats.getWeaponDamage();
                }

                // Get Arrow Damage
                float arrowDamage = 0f;
                if (arrowItem instanceof IArrowStats arrowStats) {
                    arrowDamage = arrowStats.getArrowDamage();
                }

                // Final Damage Calculation
                persistentProjectileEntity.setDamage((weaponDamage * stageMultiplier) + arrowDamage);

                // Crit Particles
                if (pullProgress == 1.0F) {
                    persistentProjectileEntity.setCritical(true);
                }
                // --- CUSTOM DAMAGE LOGIC END ---

                // Durability & Spawning
                stack.damage(1, playerEntity, LivingEntity.getSlotForHand(playerEntity.getActiveHand()));
                world.spawnEntity(persistentProjectileEntity);
            }

            // Sounds
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);

            // Ammo Usage
            if (!playerEntity.getAbilities().creativeMode) {
                arrowStack.decrement(1);
            }

            playerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        }

        // IMPORTANT: Cancel the original vanilla method so it doesn't run twice
        ci.cancel();
    }
}