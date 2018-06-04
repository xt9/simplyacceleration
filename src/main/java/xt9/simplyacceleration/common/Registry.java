package xt9.simplyacceleration.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import xt9.simplyacceleration.SimplyConstants;
import xt9.simplyacceleration.common.blocks.BlockAccelerator;
import xt9.simplyacceleration.common.items.ItemBlockAccelerator;
import xt9.simplyacceleration.common.items.ItemLinkingTool;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;

/**
 * Created by xt9 on 2018-05-26.
 */
public class Registry {
    public static BlockAccelerator blockAccelerator = new BlockAccelerator();
    public static Item blockAcceleratorItem = new ItemBlockAccelerator(blockAccelerator);
    public static Item linkingTool = new ItemLinkingTool();

    public static NonNullList<Item> items = NonNullList.create();
    public static NonNullList<Block> blocks = NonNullList.create();
    public static NonNullList<Item> itemBlocks = NonNullList.create();

    @SuppressWarnings("unchecked")
    public static void registerBlocks(IForgeRegistry registry) {
        blocks.add(blockAccelerator);
        blocks.forEach(registry::register);
        registerTileEntities();
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public static void registerItems(IForgeRegistry registry) {
        itemBlocks.add(blockAcceleratorItem);
        itemBlocks.forEach(registry::register);

        items.add(linkingTool);
        items.forEach(registry::register);
    }

    private static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityAccelerator.class, new ResourceLocation(SimplyConstants.MODID, "accelerator"));
    }
}
