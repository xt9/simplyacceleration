package xt9.simplyacceleration.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xt9.simplyacceleration.SimplyAcceleration;
import xt9.simplyacceleration.SimplyConstants;

/**
 * Created by xt9 on 2018-05-26.
 */
public class BlockBase extends Block {

    public BlockBase(String name, Material material) {
        super(material);
        setUnlocalizedName(SimplyConstants.MODID + "." + name);
        setCreativeTab(SimplyAcceleration.creativeTab);
        setRegistryName(name);
        setLightLevel(1F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(this));
    }
}
