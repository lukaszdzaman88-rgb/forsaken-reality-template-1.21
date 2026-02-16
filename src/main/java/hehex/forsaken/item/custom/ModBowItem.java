package hehex.forsaken.item.custom;

import hehex.forsaken.item.IWeaponStats;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModBowItem extends BowItem implements IWeaponStats {
    private final float weaponDamage;
    private final float projectileVelocity;
    private final float drawSpeed;

    public ModBowItem(Settings settings, float weaponDamage, float projectileVelocity, float drawSpeed) {
        super(settings);
        this.weaponDamage = weaponDamage;
        this.projectileVelocity = projectileVelocity;
        this.drawSpeed = drawSpeed;
    }

    @Override
    public float getWeaponDamage() {
        return this.weaponDamage;
    }

    @Override
    public float getProjectileVelocity() {
        return this.projectileVelocity;
    }

    @Override
    public float getDrawSpeed() {
        return this.drawSpeed;
    }

    // Punkt 3: Przenosimy logikę tooltipa tutaj (tak jak w ModScytheItem)
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.literal("Damage: " + this.weaponDamage).formatted(Formatting.RED));
        tooltip.add(Text.literal("Pull Speed: " + this.drawSpeed).formatted(Formatting.GREEN));
        // Opcjonalnie Velocity, jeśli chcesz widzieć zasięg
        // tooltip.add(Text.literal("Range Multiplier: " + this.projectileVelocity).formatted(Formatting.BLUE));
    }
}
