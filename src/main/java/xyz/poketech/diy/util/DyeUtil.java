package xyz.poketech.diy.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DyeUtil {

    private static Map<BlockState, DyeColor> stateColorCache = new HashMap<>();

    public static DyeColor getDyeForFlowerAt(World world, BlockPos pos) {

    	BlockState state = world.getBlockState(pos);
    	
    	// If possible, return early from the cache.
    	if (stateColorCache.containsKey(state)) {
    		
    		return stateColorCache.get(state);
    	}
    	
        //Grab the flower as an ItemStack
        ItemStack stack = WorldUtil.getItemStackForBlockAt(world, pos, state);

        ItemStack dye = getFlowerDye(stack, world);
        DyeColor color = DyeColor.getColor(dye);
        stateColorCache.put(state, color);
        return color;
    }

    private static ItemStack getFlowerDye(ItemStack stack, World world) {
        //Simulate the crafting of a dye from a flower
        CraftingInventory inv = new CraftingInventory(new Container(ContainerType.CRAFTING, 0) {
            @Override
            public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
                return false;
            }
        }, 1, 1);
        inv.setInventorySlotContents(0, stack);
        Optional<ICraftingRecipe> recipe = world.getRecipeManager().getRecipe(
                IRecipeType.CRAFTING, inv, world);
        return recipe.map(iCraftingRecipe -> iCraftingRecipe.getCraftingResult(inv)).orElse(null);
    }
}
