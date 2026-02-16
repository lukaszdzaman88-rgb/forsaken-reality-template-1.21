package hehex.forsaken.item.custom;

import hehex.forsaken.item.IWeaponStats;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class ModCrossbowItem extends CrossbowItem implements IWeaponStats {
    private final float weaponDamage;
    private final float projectileVelocity;
    private final float drawSpeed;

    public ModCrossbowItem(Settings settings, float weaponDamage, float projectileVelocity, float drawSpeed) {
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

    // WAŻNE: To sprawia, że gra faktycznie używa Twojego "drawSpeed" jako czasu przeładowania.
    // Vanilla dodaje +3 ticki zapasu na animację, więc zachowujemy tę konwencję.
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        // Tu można by dodać obsługę zaklęcia Szybkie Przeładowanie (Quick Charge),
        // odejmując np. 5 ticków za poziom, jeśli chcesz.
        // Na razie zwracamy czystą statystykę + 3 ticki techniczne.
        return (int) (this.drawSpeed + 3);
    }

    // Tooltip - identyczny styl jak w łuku i kosie
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.literal("Damage: " + this.weaponDamage).formatted(Formatting.RED));
        tooltip.add(Text.literal("Pull Speed: " + this.drawSpeed).formatted(Formatting.GREEN));
        // Opcjonalnie Velocity/Range
        // tooltip.add(Text.literal("Projectile Speed: " + this.projectileVelocity).formatted(Formatting.BLUE));
    }
}