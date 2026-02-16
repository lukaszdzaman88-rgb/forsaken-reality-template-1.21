package hehex.forsaken.item.custom;

import hehex.forsaken.item.IProjectileStats;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModArrowItem extends ArrowItem implements IProjectileStats {
    private final float damage;

    public ModArrowItem(Item.Settings settings, float damage) {
        super(settings);
        this.damage = damage;
    }

    @Override
    public float getProjectileDamage() {
        return this.damage;
    }

    // Punkt 3: Tooltip wewnątrz klasy
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.literal("Projectile Damage: " + this.damage).formatted(Formatting.DARK_RED));
    }
}