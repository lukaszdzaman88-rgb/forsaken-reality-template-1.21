package hehex.forsaken.mixin;

import hehex.forsaken.item.IModdedProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin implements IModdedProjectile {

    @Unique
    private float forsaken$moddedDamage = -1.0f; // -1 oznacza brak modyfikacji (fallback do vanilli)

    @Override
    public void setModdedDamage(float damage) {
        this.forsaken$moddedDamage = damage;
    }

    @Override
    public float getModdedDamage() {
        return this.forsaken$moddedDamage;
    }

    // Ten Mixin podmienia wartość obrażeń przekazywaną do metody damage() celu
    @ModifyArg(
            method = "onEntityHit",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"),
            index = 1
    )
    private float modifyHitDamage(float originalDamage) {
        // Jeśli mamy ustawione customowe obrażenia, używamy ich zamiast wyliczeń z prędkości
        if (this.forsaken$moddedDamage > 0) {
            // Możemy tutaj dodać logikę krytyczną (vanilla dodaje random do krytyków, tutaj nadpisujemy bazę)
            PersistentProjectileEntity self = (PersistentProjectileEntity) (Object) this;
            if (self.isCritical()) {
                // Opcjonalnie: Customowy mnożnik krytyka jeśli potrzebny, standardowo +50% lub inna logika
                return this.forsaken$moddedDamage * 1.5f;
            }
            return this.forsaken$moddedDamage;
        }
        return originalDamage;
    }
}