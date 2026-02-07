package hehex.forsaken.item.custom;

import hehex.forsaken.item.ICritMultiplier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModSwordItem extends SwordItem implements ICritMultiplier {
    private final float critDamageMultiplier;

    public ModSwordItem(ToolMaterial toolMaterial, Settings settings, float critDamageMultiplier) {
        super(toolMaterial, settings);
        this.critDamageMultiplier = critDamageMultiplier;
    }

    public ModSwordItem(ToolMaterial toolMaterial, Settings settings) {
        this(toolMaterial, settings, 1.5F);
    }

    @Override
    public float getCritDamageMultiplier() {
        return this.critDamageMultiplier;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        // Formats as "Crit Multiplier: 2.5x" in Gold
        tooltip.add(Text.literal("Crit Multiplier: " + this.critDamageMultiplier + "x").formatted(Formatting.GOLD));
        super.appendTooltip(stack, context, tooltip, type);
    }
}