package hehex.forsaken.item.custom;

import hehex.forsaken.item.IArrowStats;
import hehex.forsaken.item.IWeaponStats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

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
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }

        boolean hasProjectiles = playerEntity.getAbilities().creativeMode || !playerEntity.getProjectileType(stack).isEmpty();
        if (!hasProjectiles) {
            return;
        }

        int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
        float pullProgress = getPullProgress(i);

        if (!((double)pullProgress < 0.1)) {
            ItemStack arrowStack = playerEntity.getProjectileType(stack);
            if (arrowStack.isEmpty()) {
                arrowStack = new ItemStack(Items.ARROW);
            }

            if (world instanceof net.minecraft.server.world.ServerWorld) {
                ArrowItem arrowItem = (ArrowItem)(arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);
                PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, arrowStack, playerEntity, stack);

                persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, pullProgress * 3.0F, 1.0F);

                float stageMultiplier = 0.33f;
                if (pullProgress >= 1.0f) {
                    stageMultiplier = 1.0f;
                } else if (pullProgress >= 0.6f) {
                    stageMultiplier = 0.66f;
                }

                float arrowDamage = 0f;
                if (arrowItem instanceof IArrowStats arrowStats) {
                    arrowDamage = arrowStats.getArrowDamage();
                }

                persistentProjectileEntity.setDamage((this.damage * stageMultiplier) + arrowDamage);

                if (pullProgress == 1.0F) {
                    persistentProjectileEntity.setCritical(true);
                }

                stack.damage(1, playerEntity, LivingEntity.getSlotForHand(playerEntity.getActiveHand()));
                world.spawnEntity(persistentProjectileEntity);
            }

            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);

            if (!playerEntity.getAbilities().creativeMode) {
                arrowStack.decrement(1);
            }

            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Damage: " + damage).formatted(Formatting.BLUE));
        tooltip.add(Text.literal("Range: " + range).formatted(Formatting.BLUE));
        tooltip.add(Text.literal("Draw Speed: " + drawSpeed).formatted(Formatting.BLUE));
        super.appendTooltip(stack, context, tooltip, type);
    }
}