package hehex.forsaken.mixin;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {


    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;isCritical()Z"))
    private boolean disableCritDamage(PersistentProjectileEntity instance) {

        return false;
    }
}