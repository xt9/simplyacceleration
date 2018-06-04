package xt9.simplyacceleration.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xt9.simplyacceleration.SimplyConfig;
import xt9.simplyacceleration.common.blocks.BlockAccelerator;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;
import xt9.simplyacceleration.common.utils.NBTHelper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by xt9 on 2018-05-28.
 */
public class ItemLinkingTool extends ItemBase {
    public ItemLinkingTool() {
        super("linking_tool", 1);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
        list.add("Max linking range: §r" + (int) SimplyConfig.getMaxLinkingRange() + " blocks");

        if(hasTargetAccelerator(stack)) {
            BlockPos pos = BlockPos.fromLong(getTargetAcceleratorPos(stack));
            list.add("Accelerator target: §f(x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ() + ")§r");
            list.add("§fSneak + Right-click§7 to link a machine");
            list.add("Doing it a second time will un-link the machine");
            list.add("from the target accelerator");
            list.add("Clear target with §rRight-click");
        } else {
            list.add("No Accelerator target, link while sneaking");
            list.add("Clear target with §rRight-click");
        }
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
        if(!player.world.isRemote && !player.isSneaking()) {
            removeTargetAccelerator(player.getHeldItem(handIn));
            player.sendStatusMessage(new TextComponentString("§eCleared Target!§r"), true);
        }
        return super.onItemRightClick(worldIn, player, handIn);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack linkingTool = player.getHeldItem(hand);
        if(!player.world.isRemote) {
            if(player.isSneaking()) {
                if(isBlockAcceleratorAtPos(world, pos)) {
                    setTargetAccelerator(linkingTool, pos);
                    player.sendStatusMessage(new TextComponentString("§eSet accelerator target!§r"), true);
                } else if(isValidTickable(world, pos)) {
                    if(hasTargetAccelerator(linkingTool)) {
                        BlockPos acceleratorPos = BlockPos.fromLong(getTargetAcceleratorPos(linkingTool));

                        if(getBlockDistance(pos, acceleratorPos) <= SimplyConfig.getMaxLinkingRange()) {
                            if(isBlockAcceleratorAtPos(world, acceleratorPos)) {
                                TileEntityAccelerator tile = getAcceleratorFromPos(world, acceleratorPos);

                                if(tile.isUniquePosition(pos.toLong())) {
                                    if(tile.canLinkMachine()) {
                                        tile.addMachinePosition(getTileEntity(world, pos));
                                        player.sendStatusMessage(new TextComponentString("§eMachine linked!§r"), true);
                                    } else {
                                        player.sendStatusMessage(new TextComponentString("§eAccelerator link limit reached!§r"), true);
                                    }
                                } else {
                                    player.sendStatusMessage(new TextComponentString("§eMachine un-linked!§r"), true);
                                    tile.removeMachinePosition(pos.toLong());
                                }
                            }
                        } else {
                            player.sendStatusMessage(new TextComponentString("§eMachine too far away from Accelerator!§r"), true);
                        }
                    }
                }
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    /* Maths: http://www.meracalculator.com/math/distance-between-2-points(3-dim).php */
    private double getBlockDistance(BlockPos pos, BlockPos pos2) {
        double x = Math.pow((pos.getX() - pos2.getX()), 2);
        double y = Math.pow((pos.getY() - pos2.getY()), 2);
        double z = Math.pow((pos.getZ() - pos2.getZ()), 2);

        return Math.sqrt(x + y + z);
    }

    private boolean isBlockAcceleratorAtPos(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof BlockAccelerator;
    }

    private boolean isValidTickable(World world, BlockPos pos) {
        return world.getTileEntity(pos) instanceof ITickable;
    }

    private TileEntity getTileEntity(World world, BlockPos pos) {
        if(isValidTickable(world, pos)) {
            return world.getTileEntity(pos);
        } else {
            return null;
        }
    }

    private TileEntityAccelerator getAcceleratorFromPos(World world, BlockPos pos) {
        return (TileEntityAccelerator) world.getTileEntity(pos);
    }

    private boolean hasTargetAccelerator(ItemStack stack) {
        return NBTHelper.hasKey(stack, "targetAccelerator");
    }

    private long getTargetAcceleratorPos(ItemStack stack) {
        return NBTHelper.getLong(stack, "targetAccelerator", 0);
    }

    private void removeTargetAccelerator(ItemStack stack) {
        NBTHelper.removeTag(stack, "targetAccelerator");
    }

    private void setTargetAccelerator(ItemStack stack, BlockPos pos) {
        NBTHelper.setLong(stack, "targetAccelerator", pos.toLong());
    }
}
