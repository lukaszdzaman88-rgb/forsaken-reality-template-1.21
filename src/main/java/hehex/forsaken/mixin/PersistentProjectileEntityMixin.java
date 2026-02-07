package hehex.forsaken.mixin;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {

    // Redirects the check "if (this.isCritical())" inside onEntityHit.
    // By returning false, we skip the code block that adds random bonus damage.
    // The arrow itself remains "critical" for particles, but the damage math ignores it.
    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;isCritical()Z"))
    private boolean disableCritDamage(PersistentProjectileEntity instance) {
        return false;
    }
}