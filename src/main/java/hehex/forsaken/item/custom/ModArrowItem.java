package hehex.forsaken.item.custom;

import hehex.forsaken.item.IArrowStats;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModArrowItem extends ArrowItem implements IArrowStats {
    private final float damage;

    public ModArrowItem(Item.Settings settings, float damage) {
        super(settings);
        this.damage = damage;
    }

    @Override
    public float getArrowDamage() { return damage; }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Extra Damage: " + damage).formatted(Formatting.GREEN));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
