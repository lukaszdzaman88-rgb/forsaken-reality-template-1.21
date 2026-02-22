package hehex.forsaken.mixin;

import hehex.forsaken.combat.ICombatEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    // Prevent Movement if Stunned
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void cancelMovementIfStunned(Vec3d movementInput, CallbackInfo ci) {
        if (((ICombatEntity) this).getStunTicks() > 0) {
            ci.cancel();
        }
    }

    // Intercept Damage for Parry logic & Vulnerability Double Damage
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        ICombatEntity combatTarget = (ICombatEntity) thisEntity;

        // ... [Keep the existing Vulnerability and Projectile double damage logic] ...

        // 2. Parry check
        if (combatTarget.getParryTicks() > 0) {
            combatTarget.setSuccessfulParry();

            // --- NEW: Visual and Audio indication of a SUCCESSFUL parry ---
            if (thisEntity.getWorld() instanceof ServerWorld serverWorld) {
                // Play a loud metallic 'clang' sound
                serverWorld.playSound(
                        null,
                        thisEntity.getBlockPos(),
                        SoundEvents.ITEM_SHIELD_BLOCK,
                        SoundCategory.PLAYERS,
                        1.0f,
                        0.8f // Slightly lower pitch for a heavier block sound
                );

                // Spawn a burst of enchanted hit particles around the player
                serverWorld.spawnParticles(
                        ParticleTypes.ENCHANTED_HIT,
                        thisEntity.getX(), thisEntity.getBodyY(0.5), thisEntity.getZ(),
                        15, // Particle count
                        0.2, 0.2, 0.2, // Spread (X, Y, Z)
                        0.1 // Speed
                );
            }

        // 1.5 Double damage from parried projectiles
        if (source.getSource() instanceof ProjectileEntity proj && ((ICombatEntity)proj).isParriedProjectile()) {
            amount *= 2.0f;
        }

        // 2. Parry check
        if (combatTarget.getParryTicks() > 0) {
            combatTarget.setSuccessfulParry();

            if (source.getSource() instanceof ProjectileEntity projectile) {
                // Reflect projectile
                Vec3d vel = projectile.getVelocity();
                projectile.setVelocity(vel.multiply(-1.5)); // Bounce back slightly faster
                ((ICombatEntity) projectile).setParriedProjectile(true);
            } else if (source.getAttacker() instanceof LivingEntity attacker) {
                // Stun melee attacker
                ICombatEntity combatAttacker = (ICombatEntity) attacker;
                combatAttacker.applyStun(40); // 2 Seconds
                combatAttacker.setVulnerable(true); // Next hit takes double damage
            }

            // Cancel the incoming damage since it was successfully parried
            cir.setReturnValue(false);
        }
    }
}}