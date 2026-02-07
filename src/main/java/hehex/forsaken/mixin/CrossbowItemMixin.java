package hehex.forsaken.mixin;

import hehex.forsaken.item.IArrowStats;
import hehex.forsaken.item.IWeaponStats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(method = "shoot", at = @At("HEAD"), cancellable = true)
    private static void shootOverwrite(World world, LivingEntity shooter, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci) {

        ProjectileEntity projectileEntity;

        if (projectile.isOf(Items.FIREWORK_ROCKET)) {
            projectileEntity = new FireworkRocketEntity(world, projectile, shooter, shooter.getX(), shooter.getEyeY() - 0.15f, shooter.getZ(), true);
        } else {
            ArrowItem arrowItem = (ArrowItem)(projectile.getItem() instanceof ArrowItem ? projectile.getItem() : Items.ARROW);
            PersistentProjectileEntity persistentProjectile = arrowItem.createArrow(world, projectile, shooter, crossbow);

            if (shooter instanceof PlayerEntity) {
                persistentProjectile.setCritical(true);
            }

            persistentProjectile.setSound(SoundEvents.ITEM_CROSSBOW_HIT);
            persistentProjectile.setShotFromCrossbow(true);

            // FIX: Enchantment Retrieval for 1.21
            // We must get the RegistryEntry for PIERCING from the world's registry manager
            RegistryEntry<Enchantment> piercingEntry = world.getRegistryManager()
                    .get(RegistryKeys.ENCHANTMENT)
                    .getEntry(Enchantments.PIERCING)
                    .orElse(null);

            if (piercingEntry != null) {
                int piercingLevel = EnchantmentHelper.getLevel(piercingEntry, crossbow);
                if (piercingLevel > 0) {
                    persistentProjectile.setPierceLevel((byte)piercingLevel);
                }
            }

            // --- CUSTOM STATS START ---
            float weaponDamage = 9.0f;
            if (crossbow.getItem() instanceof IWeaponStats weaponStats) {
                weaponDamage = weaponStats.getWeaponDamage();
            }

            float arrowDamage = 0f;
            if (arrowItem instanceof IArrowStats arrowStats) {
                arrowDamage = arrowStats.getArrowDamage();
            }

            persistentProjectile.setDamage(weaponDamage + arrowDamage);
            persistentProjectile.setCritical(true);
            // --- CUSTOM STATS END ---

            projectileEntity = persistentProjectile;
        }

        if (shooter instanceof CrossbowUser crossbowUser) {
            // This casts the shooter to CrossbowUser (like a Pillager)
            crossbowUser.shoot(crossbowUser.getTarget(), crossbow, projectileEntity, simulated);
        } else {
            // FIX: Vector3f.X -> new Vector3f(1.0F, 0.0F, 0.0F)
            // JOML does not have static Axis fields like standard MC Vectors did
            Vector3f vector3f = shooter.getRotationVec(1.0F).toVector3f().rotate(new Quaternionf().setAngleAxis((double)(simulated * 0.017453292F), new Vector3f(1.0F, 0.0F, 0.0F)));
            projectileEntity.setVelocity((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), speed, divergence);
        }

        crossbow.damage(simulated > 0.0F ? 0 : 1, shooter, LivingEntity.getSlotForHand(shooter.getActiveHand()));
        world.spawnEntity(projectileEntity);
        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);

        ci.cancel();
    }
}