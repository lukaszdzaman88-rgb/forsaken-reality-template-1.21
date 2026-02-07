package hehex.forsaken.item;

import hehex.forsaken.ForsakenReality;
import hehex.forsaken.item.custom.ModScytheItem;
import hehex.forsaken.item.custom.ModSwordItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item WOODEN_SCYTHE = registerItem("wooden_scythe",
            new ModScytheItem(
                    ToolMaterials.WOOD,
                    new Item.Settings()
                            .attributeModifiers(ModSwordItem.createAttributeModifiers(ToolMaterials.WOOD, 7, -2.8f)),
                    2.5F,
                    2.0F
            ));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(ForsakenReality.MOD_ID, name), item);
    }

    public static void registerModItems(){


    }

}


