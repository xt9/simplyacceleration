package xt9.simplyacceleration.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xt9.simplyacceleration.SimplyConfig;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by xt9 on 2018-05-28.
 */
public class ItemBlockAccelerator extends ItemBlock {
    public ItemBlockAccelerator(Block block) {
        super(block);
        setRegistryName("accelerator");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("Increases the speed of linked Machines");
        tooltip.add("Power consumption: §r" + SimplyConfig.getAcceleratorEnergyConsumption() + " FE/t");
        tooltip.add("Max machine links: §r" + SimplyConfig.getMaxLinkedMachines());
        tooltip.add("Max speed multiplier: §r" + SimplyConfig.getMaxSpeedMultiplier());
    }
}
