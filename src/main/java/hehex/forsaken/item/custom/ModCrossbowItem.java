package hehex.forsaken.item.custom;

import hehex.forsaken.item.IWeaponStats;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModCrossbowItem extends CrossbowItem implements IWeaponStats {
    private final float damage;
    private final float range;

    public ModCrossbowItem(Settings settings, float damage, float range) {
        super(settings);
        this.damage = damage;
        this.range = range;
    }

    @Override
    public float getWeaponDamage() { return damage; }
    @Override
    public int getRange() { return (int) range; }
    @Override
    public float getDrawSpeed() { return 1.0f; } // Not used for crossbow logic directly

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Damage: " + damage).formatted(Formatting.BLUE));
        tooltip.add(Text.literal("Range: " + range).formatted(Formatting.BLUE));
        super.appendTooltip(stack, context, tooltip, type);
    }
}