package hehex.forsaken.item.custom;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Identifier;

public class ModMeleeItem extends SwordItem {
    private final float critMultiplier;

    public ModMeleeItem(ToolMaterial material, float attackDamage, float attackSpeed, float reach, float critMultiplier, Settings settings) {
        super(material, settings.attributeModifiers(createAttributes(attackDamage, attackSpeed, reach)));
        this.critMultiplier = critMultiplier;
    }

    private static AttributeModifiersComponent createAttributes(float damage, float speed, float reach) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, damage, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, speed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                // 1.21 Vanilla Reach Attribute
                .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.of("forsaken", "reach"), reach, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    public float getCritMultiplier() {
        return this.critMultiplier;
    }

    // Called automatically when dealing damage to a vulnerable (parried) target
    public void onStrengthenedHit(LivingEntity target, PlayerEntity attacker) {
        // To be overridden by specific weapons
    }
}