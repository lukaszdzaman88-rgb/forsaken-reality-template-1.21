package hehex.forsaken.mixin;

import hehex.forsaken.combat.ICombatEntity;
import hehex.forsaken.item.custom.ModMeleeItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class CombatEntityMixin implements ICombatEntity {
    @Unique private int parryTicks = 0;
    @Unique private int parryCooldown = 0;
    @Unique private boolean parrySuccessful = false;

    @Unique private int stunTicks = 0;
    @Unique private boolean isVulnerable = false;
    @Unique private boolean isParriedProjectile = false;

    @Override
    public void startParry() {
        if (parryCooldown <= 0 && ((Entity)(Object)this) instanceof PlayerEntity player) {
            if (player.getMainHandStack().getItem() instanceof ModMeleeItem) {
                this.parryTicks = 15;
                this.parrySuccessful = false;

                // --- NEW: Indication that parry has started ---
                // Play a 'whoosh' or 'equip' sound to indicate the weapon is readied
                player.getWorld().playSound(
                        null,
                        player.getBlockPos(),
                        SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, // A swoosh sound
                        SoundCategory.PLAYERS,
                        0.8f,
                        1.5f // Higher pitch makes it sound faster
                );

                // Visually swing the hand
                player.swingHand(Hand.MAIN_HAND, true);
            }
        }
    }
    @Override
    public void setSuccessfulParry() {
        this.parrySuccessful = true;
        this.parryTicks = 0; // End parry immediately upon success
        this.parryCooldown = 600; // 30 seconds
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        // Handle Cooldown and Parry timer
        if (parryCooldown > 0) parryCooldown--;
        if (parryTicks > 0) {
            parryTicks--;
            if (parryTicks <= 0 && !parrySuccessful) {
                parryCooldown = 300; // 15 seconds if missed
            }
        }

        // Handle Stun
        if (stunTicks > 0) stunTicks--;
    }

    // --- Interface Getters/Setters ---
    @Override public int getParryTicks() { return parryTicks; }
    @Override public int getStunTicks() { return stunTicks; }
    @Override public void applyStun(int ticks) { this.stunTicks = ticks; }
    @Override public boolean isVulnerable() { return isVulnerable; }
    @Override public void setVulnerable(boolean v) { this.isVulnerable = v; }
    @Override public boolean isParriedProjectile() { return isParriedProjectile; }
    @Override public void setParriedProjectile(boolean p) { this.isParriedProjectile = p; }
}