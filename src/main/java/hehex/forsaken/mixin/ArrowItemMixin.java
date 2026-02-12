package hehex.forsaken.mixin;

import hehex.forsaken.item.IProjectileStats;
import net.minecraft.item.ArrowItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArrowItem.class)
public class ArrowItemMixin implements IProjectileStats {
    @Override
    public float getProjectileDamage() {
        return 4.0f; // Domyślna wartość dla wszystkich strzał
    }
}