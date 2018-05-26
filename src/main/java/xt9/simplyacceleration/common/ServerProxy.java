package xt9.simplyacceleration.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import xt9.simplyacceleration.SimplyAcceleration;
import xt9.simplyacceleration.SimplyConstants;
import xt9.simplyacceleration.common.inventory.ContainerAccelerator;
import xt9.simplyacceleration.common.tiles.IGuiTile;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;

import javax.annotation.Nullable;

/**
 * Created by xt9 on 2018-05-26.
 */
public class ServerProxy implements IGuiHandler {

    public void preInit() {
    }

    public void registerRenderers() {}

    public void registerModels() {}

    public void openTileEntityGui(World world, EntityPlayer player, IGuiTile te, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        // Notify on open so we trigger the TE's getUpdateTag
        world.notifyBlockUpdate(pos, state, state, 3);
        player.openGui(SimplyAcceleration.instance, te.getGuiID(), player.world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case SimplyConstants.ACCELERATOR_GUI_ID: return new ContainerAccelerator((TileEntityAccelerator) world.getTileEntity(new BlockPos(x, y, z)), player.inventory, world);
            default: return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }


}
