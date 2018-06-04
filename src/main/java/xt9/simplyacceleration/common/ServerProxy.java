package xt9.simplyacceleration.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import xt9.simplyacceleration.SimplyConstants;
import xt9.simplyacceleration.common.inventory.ContainerAccelerator;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;

import javax.annotation.Nullable;

/**
 * Created by xt9 on 2018-05-26.
 */
public class ServerProxy implements IGuiHandler {

    public void preInit() {}

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
    public void spawnParticle(World world, double x, double y, double z, double mx, double my, double mz) {}
    public void registerModels() {}
    public void registerRenderers() {}

}
