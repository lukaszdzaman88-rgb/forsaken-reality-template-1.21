package hehex.forsaken.item.custom;

import hehex.forsaken.item.IWeaponStats;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModBowItem extends BowItem implements IWeaponStats {
    private final float damage;
    private final float range;
    private final float drawSpeed;

    public ModBowItem(Settings settings, float damage, float range, float drawSpeed) {
        super(settings);
        this.damage = damage;
        this.range = range;
        this.drawSpeed = drawSpeed;
    }

    @Override
    public float getWeaponDamage() { return damage; }
    @Override
    public int getRange() { return (int) range; }
    @Override
    public float getDrawSpeed() { return drawSpeed; }

    @Override
    public int getMaxUseTime(ItemStack stack, net.minecraft.entity.LivingEntity user) {
        // Adjust draw time based on speed (Vanilla default is 72000)
        // We interact with pull progress math in the Mixin, but this can remain standard
        return 72000;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Damage: " + damage).formatted(Formatting.BLUE));
        tooltip.add(Text.literal("Range: " + range).formatted(Formatting.BLUE));
        tooltip.add(Text.literal("Draw Speed: " + drawSpeed).formatted(Formatting.BLUE));
        super.appendTooltip(stack, context, tooltip, type);
    }
}