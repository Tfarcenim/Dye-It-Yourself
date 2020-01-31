package xyz.poketech.diy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.poketech.diy.item.DyeBrushItem;

public class DIYItems {

    static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, DyeItYourself.MODID);

    public static final RegistryObject<Item> DYE_BRUSH = ITEMS.register("dye_brush", () ->
            new DyeBrushItem(
                    new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1)
            )
    );

}
