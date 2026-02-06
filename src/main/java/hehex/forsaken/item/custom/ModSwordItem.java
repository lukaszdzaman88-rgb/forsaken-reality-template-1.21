package hehex.forsaken.item.custom;

import hehex.forsaken.item.ICritMultiplier;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.state.property.Properties;

public class ModSwordItem extends SwordItem implements ICritMultiplier {
    private final float critDamageMultiplier;

    public ModSwordItem(ToolMaterial toolMaterial, Settings settings, float critDamageMultiplier) {
        super(toolMaterial, settings);
        this.critDamageMultiplier = critDamageMultiplier;
    }

    // Constructor with default vanilla settings (if needed)
    public ModSwordItem(ToolMaterial toolMaterial, Settings settings) {
        this(toolMaterial, settings, 1.5F);
    }

    @Override
    public float getCritDamageMultiplier() {
        return this.critDamageMultiplier;
    }
}
