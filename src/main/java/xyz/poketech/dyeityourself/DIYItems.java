package xyz.poketech.dyeityourself;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.poketech.dyeityourself.item.DyeBrushItem;

public class DIYItems {

    static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DyeItYourself.MODID);

    public static final RegistryObject<Item> DYE_BRUSH = ITEMS.register("dye_brush", () ->
            new DyeBrushItem(
                    new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1)
            )
    );

}
