package hehex.forsaken.item.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ModScytheItem extends ModSwordItem {
    private final float lifesteal;

    public ModScytheItem(ToolMaterial toolMaterial, Settings settings, float critDamageMultiplier, float lifesteal) {
        super(toolMaterial, settings, critDamageMultiplier);
        this.lifesteal = lifesteal;
    }

    // Getter used by the Mixin
    public float getLifesteal() {
        return this.lifesteal;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        // This will add the Crit Multiplier line from ModSwordItem first
        super.appendTooltip(stack, context, tooltip, type);

        // Then add the Lifesteal line in Red
        if (this.lifesteal > 0) {
            tooltip.add(Text.literal("Lifesteal: " + this.lifesteal).formatted(Formatting.RED));
        }
    }
}