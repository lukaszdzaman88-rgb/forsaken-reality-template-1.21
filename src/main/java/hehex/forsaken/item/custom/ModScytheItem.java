package hehex.forsaken.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

public class ModScytheItem extends ModSwordItem {
    private final float lifesteal;

    public ModScytheItem(ToolMaterial toolMaterial, Item.Settings settings, float critDamageMultiplier, float lifesteal) {
        // Pass the material, settings, and crit multiplier to the ModSwordItem (parent)
        super(toolMaterial, settings, critDamageMultiplier);
        this.lifesteal = lifesteal;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Apply lifesteal if the value is greater than 0
        if (this.lifesteal > 0) {
            attacker.heal(this.lifesteal);
        }

        // Call super to ensure standard durability loss behavior from SwordItem/ModSwordItem
        return super.postHit(stack, target, attacker);
    }
}
