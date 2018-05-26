package xt9.simplyacceleration.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import xt9.simplyacceleration.SimplyConstants;
import xt9.simplyacceleration.common.blocks.BlockAccelerator;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;

/**
 * Created by xt9 on 2018-05-26.
 */
public class Registry {
    public static NonNullList<Item> items = NonNullList.create();
    public static NonNullList<Block> blocks = NonNullList.create();

    @SuppressWarnings("unchecked")
    public static void registerBlocks(IForgeRegistry registry) {
        blocks.add(BlockAccelerator.INSTANCE);
        blocks.forEach(registry::register);

        registerTileEntities();
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public static void registerItems(IForgeRegistry registry) {
        blocks.forEach(block -> registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName())));

        items.forEach(registry::register);
    }

    private static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityAccelerator.class, new ResourceLocation(SimplyConstants.MODID, "accelerator"));
    }
}
