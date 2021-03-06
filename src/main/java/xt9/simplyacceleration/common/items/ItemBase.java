package xt9.simplyacceleration.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xt9.simplyacceleration.SimplyAcceleration;
import xt9.simplyacceleration.SimplyConstants;

/**
 * Created by xt9 on 2018-05-28.
 */
public class ItemBase extends Item {
    public String itemName;

    public ItemBase(String name, int stackSize) {
        setUnlocalizedName(SimplyConstants.MODID + "." + name);
        setCreativeTab(SimplyAcceleration.creativeTab);
        setRegistryName(name);
        setMaxStackSize(stackSize);
        this.itemName = name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if(isInCreativeTab(tab)) {
            list.add(new ItemStack(this));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName();
    }
}
