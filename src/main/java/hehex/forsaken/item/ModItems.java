package hehex.forsaken.item;

import hehex.forsaken.ForsakenReality;
import hehex.forsaken.item.custom.ModMeleeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static void registerModItems() {
    }
public static final Item EXAMPLE_WEAPON = registerItem("example_weapon",
        new ModMeleeItem(
                ToolMaterials.WOOD,
                1,
                2,
                3,
                4,
                new Item.Settings()
        ));







    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(ForsakenReality.MOD_ID, name), item);
    }


}


